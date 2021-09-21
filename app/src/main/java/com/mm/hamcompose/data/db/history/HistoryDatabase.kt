package com.mm.hamcompose.data.db.history

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mm.hamcompose.data.bean.HistoryRecord
import com.mm.hamcompose.data.db.DbConst

@Database(entities = [HistoryRecord::class], version = DbConst.dbVersion)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}