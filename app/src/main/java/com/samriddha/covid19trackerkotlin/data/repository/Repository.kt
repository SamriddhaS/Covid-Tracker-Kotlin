package com.samriddha.covid19trackerkotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samriddha.covid19trackerkotlin.data.db.AllCountryDao
import com.samriddha.covid19trackerkotlin.data.db.GlobalDataDao
import com.samriddha.covid19trackerkotlin.data.db.IndiaDataDao
import com.samriddha.covid19trackerkotlin.data.db.entity.*
import com.samriddha.covid19trackerkotlin.data.network.ApiService
import com.samriddha.covid19trackerkotlin.data.network.IndiaDetailApiService
import com.samriddha.covid19trackerkotlin.data.network.SafeApiRequest
import com.samriddha.covid19trackerkotlin.others.EpochTimeProvider
import com.samriddha.covid19trackerkotlin.pojo.DistrictName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(
    private val globalDataDao: GlobalDataDao,
    private val allCountryDao: AllCountryDao,
    private val indiaDataDao: IndiaDataDao,
    private val apiService: ApiService,
    private val indiaDataApiService: IndiaDetailApiService
) : SafeApiRequest() {

    suspend fun getGlobalData(): LiveData<GlobalData> {

        return withContext(Dispatchers.IO) {
            return@withContext globalDataDao.getLocalGlobalData()
        }

    }

    suspend fun getAllCountry(): LiveData<List<CountryData>> {

        return withContext(Dispatchers.IO) {
            return@withContext allCountryDao.getLocalAllCountry()
        }

    }

    suspend fun getIndiaData(): LiveData<IndiaData> {
        return withContext(Dispatchers.IO) {

            return@withContext indiaDataDao.getLocalIndiaData()
        }
    }

    suspend fun getIndiaDataByTime(): LiveData<List<IndiaDataByTime>> {

        return withContext(Dispatchers.IO) {

            return@withContext indiaDataDao.getLocalIndiaDataByTime()
        }
    }

    suspend fun getIndiaStatesData(): LiveData<List<IndiaStateData>> {
        return withContext(Dispatchers.IO) {

            return@withContext indiaDataDao.getLocalIndiaStates()
        }
    }

    suspend fun getAllDistrictsData(): LiveData<DistrictName>? {

        return withContext(Dispatchers.IO) {

            val data = MutableLiveData<DistrictName>()

            val allDistricts = safeApiRequest { indiaDataApiService.getAllDistricts() }
            data.postValue(allDistricts)
            Log.e("Network", "Neetwork Call Made")
            return@withContext data
        }

    }

    suspend fun updateIndiaDetailData() {

        withContext(Dispatchers.IO) {
            if (indiaDataDao.getLocalIndiaDataNonLive() == null) {
                fetchIndiaAndStateData()
                Log.e("Network", "India: Called from data null empty")
                return@withContext
            }
            val fetchTime = indiaDataDao.getLocalIndiaDataNonLive().networkFetchTime
            if (isNetworkFetchNeeded(fetchTime)) {
                fetchIndiaAndStateData()
                Log.e("Network", "India: Called from time crossed")
            }
        }

    }

    private suspend fun fetchIndiaAndStateData() {

        /////api call from "ApiService"////////////////////////////////////////////

        val data = safeApiRequest { apiService.getIndiaData() }
        data.networkFetchTime = EpochTimeProvider.getCurrentEpoch()
        indiaDataDao.insertIndiaData(data)

        /////////////////////////////////////////////////////////////////////////

        /////api call from "IndiaDetailApiService" which has IndiaStateData and IndiaDataByTime///////

        val detailData =
            safeApiRequest { indiaDataApiService.getDetailIndiaData() }//this response has both List<IndiaDataByTime> and List<IndiaStateData>.

        indiaDataDao.deleteIndiaDataByTime()
        indiaDataDao.deleteLocalIndiaStates()

        detailData.stateDataList?.let { indiaDataDao.insertIndiaStateData(it) }
        detailData.dataByTimeList?.let { indiaDataDao.insertIndiaDataByTime(it) }

    }

    suspend fun updateAllCountry(sortingKey: String, keyChanged: Boolean) {

        withContext(Dispatchers.IO) {
            if (allCountryDao.getLocalAllCountryNonLive().isNullOrEmpty() || keyChanged) {
                Log.e("Network", "Called from data null empty/key changed")
                fetchAllCountryList(sortingKey)
                return@withContext
            }

            val lastFetchTime = allCountryDao.getLocalAllCountryNonLive().first().networkFetchTime
            if (isNetworkFetchNeeded(lastFetchTime)) {
                Log.e("Network", "Time limit crossed")
                fetchAllCountryList(sortingKey)
            }
        }

    }

    private suspend fun fetchAllCountryList(sortingKey: String) {

        val allCountry = safeApiRequest { apiService.getAllCountryList(sortingKey) }
        for (item in allCountry.indices) {
            allCountry[item].networkFetchTime = EpochTimeProvider.getCurrentEpoch()
        }
        allCountryDao.deleteAllCountryData()
        allCountryDao.insertAllCountryList(allCountry)

    }

    suspend fun updateGlobalData() {

        if (globalDataDao.getLocalGlobalDataNonLive() == null) {
            Log.e("Network", "Global:Called from data null empty/key changed")
            fetchGlobalData()
            return
        }

        val lastFetchTime = globalDataDao.getLocalGlobalDataNonLive().networkFetchTime
        if (isNetworkFetchNeeded(lastFetchTime)) {
            Log.e("Network", "Global:Time limit crossed")
            fetchGlobalData()
        }
    }

    private suspend fun fetchGlobalData() {

        val data = safeApiRequest { apiService.getGlobalData() }
        data.networkFetchTime = EpochTimeProvider.getCurrentEpoch()
        globalDataDao.insertGlobalData(data)

    }

    private fun isNetworkFetchNeeded(lastFetchTime: Long): Boolean {

        val time = EpochTimeProvider.getTimeMinus(EpochTimeProvider.getCurrentEpoch(), 300)

        return time >= lastFetchTime
    }

}