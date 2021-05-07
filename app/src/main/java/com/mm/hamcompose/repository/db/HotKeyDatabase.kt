package com.mm.hamcompose.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mm.hamcompose.bean.Hotkey

@Database(entities = [Hotkey::class], version = 1)
abstract class HotkeyDatabase: RoomDatabase() {
    abstract fun hotkeyDao(): HotkeysDao
}