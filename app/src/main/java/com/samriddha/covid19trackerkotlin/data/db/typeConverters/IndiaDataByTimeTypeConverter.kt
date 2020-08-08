package com.samriddha.covid19trackerkotlin.data.db.typeConverters

import androidx.room.TypeConverter
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaDataByTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class IndiaDataByTimeTypeConverter {

    @TypeConverter
    fun fromAllCountryList(dataByTime: List<IndiaDataByTime?>?): String? {
        if (dataByTime == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IndiaDataByTime?>?>() {}.getType()
        return gson.toJson(dataByTime, type)
    }

    @TypeConverter
    fun toAllCountryList(dataByTime: String?): List<IndiaDataByTime>? {
        if (dataByTime == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IndiaDataByTime?>?>() {}.getType()
        return gson.fromJson<List<IndiaDataByTime>>(dataByTime, type)
    }

}