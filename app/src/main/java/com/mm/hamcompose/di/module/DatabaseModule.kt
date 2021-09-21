package com.mm.hamcompose.di.module

import androidx.room.Room
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.data.db.DbConst
import com.mm.hamcompose.data.db.history.HistoryDatabase
import com.mm.hamcompose.data.db.hotkey.HotkeyDatabase
import com.mm.hamcompose.data.db.user.UserInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideHotkeyDataBase(): HotkeyDatabase {
        return Room.databaseBuilder(HamApp.CONTEXT, HotkeyDatabase::class.java, DbConst.hotKeyDbName)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserInfoDataBase(): UserInfoDatabase {
        return Room.databaseBuilder(HamApp.CONTEXT, UserInfoDatabase::class.java, DbConst.userDbName)
            .build()
    }

    @Singleton
    @Provides
    fun provideHistoryDataBase(): HistoryDatabase {
        return Room.databaseBuilder(HamApp.CONTEXT, HistoryDatabase::class.java, DbConst.historyDbName)
            .build()
    }

}