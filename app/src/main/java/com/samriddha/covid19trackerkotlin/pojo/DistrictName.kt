package com.samriddha.covid19trackerkotlin.pojo


import java.util.*


data class DistrictName (
    var districtValue: Map<String, DistrictValue>){

    init {
        districtValue = HashMap()
    }

}