package com.samriddha.covid19trackerkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaData
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaDataByTime
import com.samriddha.covid19trackerkotlin.data.db.entity.IndiaStateData
@Dao
interface IndiaDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIndiaStateData(indiaStates:List<IndiaStateData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIndiaDataByTime(dataByTime:List<IndiaDataByTime>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIndiaData(indiaData:IndiaData)

    @Query("select * from india_state_data_table")
    fun getLocalIndiaStates():LiveData<List<IndiaStateData>>

    @Query("select * from india_state_data_table")
    fun getLocalIndiaStatesNonLive():List<IndiaStateData>

    @Query("delete from india_state_data_table")
    fun deleteLocalIndiaStates()

    @Query("select * from india_data_by_time_table")
    fun getLocalIndiaDataByTime():LiveData<List<IndiaDataByTime>>

    @Query("delete from india_data_by_time_table")
    fun deleteIndiaDataByTime()

    @Query("select * from india_data_table")
    fun getLocalIndiaData():LiveData<IndiaData>

    @Query("select * from india_data_table")
    fun getLocalIndiaDataNonLive():IndiaData

}