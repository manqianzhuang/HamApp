package com.mm.hamcompose.data.db.hotkey

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mm.hamcompose.data.bean.Hotkey
import com.mm.hamcompose.data.db.DbConst

@Database(entities = [Hotkey::class], version = DbConst.dbVersion)
abstract class HotkeyDatabase: RoomDatabase() {
    abstract fun hotkeyDao(): HotkeysDao
}