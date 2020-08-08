package com.samriddha.covid19trackerkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samriddha.covid19trackerkotlin.data.db.entity.GlobalData
import com.samriddha.covid19trackerkotlin.data.db.entity.PRIMARY_KEY

@Dao
interface GlobalDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGlobalData(globalData: GlobalData)

    @Query("select * from global_data_table where primaryKey=$PRIMARY_KEY")
    fun getLocalGlobalData():LiveData<GlobalData>

    @Query("select * from global_data_table where primaryKey=$PRIMARY_KEY")
    fun getLocalGlobalDataNonLive(): GlobalData
}