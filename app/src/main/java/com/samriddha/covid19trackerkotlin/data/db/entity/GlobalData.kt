package com.samriddha.covid19trackerkotlin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PRIMARY_KEY = 0
@Entity(tableName = "global_data_table")
data class GlobalData(
    val updated: String,
    val cases: String,
    val todayCases: String,
    val deaths: String,
    val todayDeaths: String,
    val recovered: String,
    val active: String,
    val critical: String,
    val casesPerOneMillion: String,
    val deathsPerOneMillion: String,
    val tests: String,
    val testsPerOneMillion: String,
    val affectedCountries: String?
) {
    @PrimaryKey(autoGenerate = false)
    var primaryKey: Int = PRIMARY_KEY

    var networkFetchTime: Long = 0
}