package com.samriddha.covid19trackerkotlin.pojo

import com.google.gson.annotations.SerializedName
import java.util.*

data class DistrictValue(
    @SerializedName("districtData")
    var districts: Map<String, Districts>
) {
    init {
        districts = HashMap()
    }
}