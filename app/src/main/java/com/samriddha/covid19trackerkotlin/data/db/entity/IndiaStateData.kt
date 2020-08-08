package com.samriddha.covid19trackerkotlin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "india_state_data_table")
class IndiaStateData(
    @SerializedName("active")
    val activeCase: String?,

    @SerializedName("confirmed")
    val totalCase: String?,

    @SerializedName("deaths")
    val totalDeaths: String?,

    @SerializedName("recovered")
    val totalRecovered: String?,

    @SerializedName("deltaconfirmed")
    val todayCase: String?,

    @SerializedName("deltadeaths")
    val todayDeaths: String?,

    @SerializedName("deltarecovered")
    val todayRecovered: String?,

    @SerializedName("state")
    val stateName: String?,

    @SerializedName("statecode")
    val stateCode: String?,

    @SerializedName("statenotes")
    val stateNotes: String?,

    @SerializedName("lastupdatedtime")
    val lastUpdated: String?,

    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int
) : Serializable