package com.samriddha.covid19trackerkotlin.pojo

import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaDataByTime
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData
import com.google.gson.annotations.SerializedName

data class IndiaDetailResponse(
    @SerializedName("cases_time_series")
    val dataByTimeList: List<IndiaDataByTime>?,

    @SerializedName("statewise")
    val stateDataList: List<IndiaStateData>?
)