package com.samriddha.covid19trackerkotlin.others

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

// Keys For saving api sorting data
const val KEY_TOTAL_CASE = "TOTAL_CASE"
const val KEY_ACTIVE_CASE = "ACTIVE_CASE"
const val KEY_TODAY_CASE = "TODAY_CASE"
const val KEY_RECOVERED = "RECOVERED"
const val KEY_TOTAL_DEATHS = "TOTAL_DEATHS"
const val KEY_TODAY_DEATHS = "TODAY_DEATHS"
const val KEY_A_Z = "A_Z"
const val API_SORT_KEY = "API_SORTING_INFO"

class SharedPrefManager(context: Context) {
    private var preferences: SharedPreferences? = null

    init {
        if (preferences == null) {
            preferences =
                context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        }
        if (preferences != null && !preferences!!.contains(KEY_ACTIVE_CASE)
            && !preferences!!.contains(KEY_RECOVERED)
            && !preferences!!.contains(KEY_TODAY_CASE)
            && !preferences!!.contains(KEY_TOTAL_CASE)
            && !preferences!!.contains(KEY_TODAY_DEATHS)
            && !preferences!!.contains(KEY_TOTAL_DEATHS)
            && !preferences!!.contains(KEY_A_Z)
        ) {
            insertApiSortData()
        }
    }

    private fun insertApiSortData() {
        val editor = preferences!!.edit()
        editor.putString(KEY_TOTAL_CASE, "cases")
        editor.putString(KEY_ACTIVE_CASE, "active")
        editor.putString(KEY_TODAY_CASE, "todayCases")
        editor.putString(KEY_RECOVERED, "recovered")
        editor.putString(KEY_TOTAL_DEATHS, "deaths")
        editor.putString(KEY_TODAY_DEATHS, "todayDeaths")
        editor.putString(KEY_A_Z, "null")
        editor.apply()
    }

    fun write(key: String?, value: String?) {
        val editor = preferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun read(key: String?): String? {
        return preferences!!.getString(key, "null")
    }

    fun getCurrentKey():String?{
        return preferences!!.getString(API_SORT_KEY,"null")
    }

}