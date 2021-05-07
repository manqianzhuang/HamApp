package com.mm.hamcompose.di.module

import androidx.room.Room
import com.mm.hamcompose.HamApp
import com.mm.hamcompose.repository.db.HotkeyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideHotkeyDataBase(): HotkeyDatabase {
        return Room.databaseBuilder(HamApp.CONTEXT, HotkeyDatabase::class.java, "ham_app_db").build()
    }

}