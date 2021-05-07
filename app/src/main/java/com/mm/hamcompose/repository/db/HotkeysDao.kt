package com.mm.hamcompose.repository.db

import androidx.room.*
import com.mm.hamcompose.bean.Hotkey

@Dao
interface HotkeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotkeys(vararg keys: Hotkey)

    @Update
    suspend fun updateHotkeys(vararg keys: Hotkey)

    @Delete
    suspend fun deleteKeys(vararg keys: Hotkey)

    @Query("SELECT * FROM hot_key")
    suspend fun loadAllKeys(): Array<Hotkey>

    @Query("DELETE FROM hot_key")
    suspend fun deleteAll()

}