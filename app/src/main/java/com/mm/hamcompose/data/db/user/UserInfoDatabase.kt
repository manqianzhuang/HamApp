package com.mm.hamcompose.data.db.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mm.hamcompose.data.bean.IntTypeConverter
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.db.DbConst

@Database(entities = [UserInfo::class], version = DbConst.dbVersion)
@TypeConverters(IntTypeConverter::class)
abstract class UserInfoDatabase: RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
}