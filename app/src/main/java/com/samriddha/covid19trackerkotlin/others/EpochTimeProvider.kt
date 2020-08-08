package com.samriddha.covid19trackerkotlin.others

import java.util.*

object EpochTimeProvider {

    fun getCurrentEpoch():Long = System.currentTimeMillis() / 1000

    fun getTimeMinus(epochTime:Long, duration:Int):Long{

        val date = Date(epochTime-(duration * 60))

        return date.time
    }

}