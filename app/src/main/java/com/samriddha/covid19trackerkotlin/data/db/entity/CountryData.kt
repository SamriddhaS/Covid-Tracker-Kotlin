package com.samriddha.covid19trackerkotlin.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samriddha.covid19trackerkotlin.pojo.CountryInfo
import java.io.Serializable

@Entity(tableName = "all_country_table")
data class CountryData(val updated: String?,
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
                       val continent: String?,
                       @PrimaryKey(autoGenerate = true)
                       val primaryKey: Int
): Serializable {
    var networkFetchTime: Long = 0
}