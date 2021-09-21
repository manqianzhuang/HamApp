package com.mm.hamcompose.data.db.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mm.hamcompose.data.bean.HistoryRecord

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(vararg history: HistoryRecord)

    @Query("SELECT * FROM history")
    suspend fun queryAll(): Array<HistoryRecord>

    @Query("DELETE FROM history")
    suspend fun deleteAll()

}