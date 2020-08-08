package com.samriddha.covid19trackerkotlin.data.db.typeConverters

import androidx.room.TypeConverter
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class IndiaStateListTypeConverter {

    @TypeConverter
    fun fromAllCountryList(allStates: List<IndiaStateData?>?): String? {
        if (allStates == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IndiaStateData?>?>() {}.getType()
        return gson.toJson(allStates, type)
    }

    @TypeConverter
    fun toAllCountryList(allStatesString: String?): List<IndiaStateData>? {
        if (allStatesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<IndiaStateData?>?>() {}.getType()
        return gson.fromJson<List<IndiaStateData>>(allStatesString, type)
    }

}