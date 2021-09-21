package com.mm.hamcompose.data.db.user

import androidx.room.*
import com.mm.hamcompose.data.bean.UserInfo
import com.mm.hamcompose.data.db.DbConst
import retrofit2.http.DELETE

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Update
    suspend fun updateUserInfo(userInfo: UserInfo)

    @Query("SELECT * FROM user_info")
    suspend fun queryUserInfo(): List<UserInfo?>

    @Delete(entity = UserInfo::class)
    suspend fun deleteUserInfo(vararg userInfo: UserInfo): Int

    @Query("DELETE FROM user_info")
    suspend fun deleteAllUserInfo()



}