package com.samriddha.covid19trackerkotlin.ui.india.districts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.samriddha.covid19trackerkotlin.data.repository.Repository
import com.samriddha.covid19trackerkotlin.pojo.DistrictName

class AllDistrictsViewModel(private val repository: Repository): ViewModel() {

    suspend fun fetchDistrictsData():LiveData<DistrictName>?{

        return repository.getAllDistrictsData()
    }

}