package com.samriddha.covid19trackerkotlin.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samriddha.covid19trackerkotlin.pojo.CountryInfo

const val INDIA_DATA_PRIMARY_KEY = 1

@Entity(tableName = "india_data_table")
data class IndiaData(
    val updated: String?,
    val country: String?,
    @Embedded(prefix = "info_")
    val countryInfo: CountryInfo?,
    val cases: String?,
    val todayCases: String?,
    val deaths: String?,
    val todayDeaths: String?,
    val recovered: String?,
    val active: String?,
    val critical: String?,
    val casesPerOneMillion: String?,
    val deathsPerOneMillion: String?,
    val tests: String?,
    val testsPerOneMillion: String?,
    val continent: String?

){
    var networkFetchTime: Long = 0

    @PrimaryKey(autoGenerate = false)
    var primaryKey: Int = INDIA_DATA_PRIMARY_KEY
}