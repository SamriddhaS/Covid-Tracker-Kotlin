package com.samriddha.covid19trackerkotlin.data.db.typeConverters

import androidx.room.TypeConverter
import com.samriddha.covid19trackerkotlin.data.db.entity.CountryData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AllCountryListTypeConverter {

    @TypeConverter
    fun fromAllCountryList(allCountry: List<CountryData?>?): String? {
        if (allCountry == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CountryData?>?>() {}.getType()
        return gson.toJson(allCountry, type)
    }

    @TypeConverter
    fun toAllCountryList(allCountryString: String?): List<CountryData>? {
        if (allCountryString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<CountryData?>?>() {}.getType()
        return gson.fromJson<List<CountryData>>(allCountryString, type)
    }

}