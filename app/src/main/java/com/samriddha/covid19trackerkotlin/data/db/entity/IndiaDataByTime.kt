package com.samriddha.covid19trackerkotlin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "india_data_by_time_table")
data class IndiaDataByTime(
    @SerializedName("totalconfirmed")
    val totalCase: String?,

    @SerializedName("totaldeceased")
    val totalDeath: String?,

    @SerializedName("totalrecovered")
    val totalRecovered: String?,

    @SerializedName("date")
    val date: String?,

    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int
)
