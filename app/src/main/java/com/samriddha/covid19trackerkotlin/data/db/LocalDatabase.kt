package com.samriddha.covid19trackerkotlin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samriddha.covid19trackerkotlin.data.db.entity.*
import com.samriddha.covid19trackerkotlin.data.db.typeConverters.*

@TypeConverters(
    AllCountryListTypeConverter::class,
    IndiaStateListTypeConverter::class,
    IndiaDataByTimeTypeConverter::class
)

@Database(
    entities = [GlobalData::class
        , CountryData::class
        , IndiaStateData::class
        , IndiaData::class
        , IndiaDataByTime::class]
    , version = 1
)

abstract class LocalDatabase : RoomDatabase() {

    abstract fun globalDataDao(): GlobalDataDao
    abstract fun allCountryDao(): AllCountryDao
    abstract fun indiaDataDao(): IndiaDataDao


    companion object {
        @Volatile
        private var instance: LocalDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "forecast.db"
            ).build()
    }

}