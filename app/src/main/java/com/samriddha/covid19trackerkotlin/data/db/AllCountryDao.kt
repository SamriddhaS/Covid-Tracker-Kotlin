package com.samriddha.covid19trackerkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData

@Dao
interface AllCountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCountryList(allCountry:List<CountryData>)

    @Query("delete from all_country_table")
    fun deleteAllCountryData()

    @Query("select * from all_country_table")
    fun getLocalAllCountry():LiveData<List<CountryData>>

    @Query("select * from all_country_table")
    fun getLocalAllCountryNonLive():List<CountryData>
}