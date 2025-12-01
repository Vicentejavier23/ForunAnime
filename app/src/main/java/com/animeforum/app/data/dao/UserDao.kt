package com.animeforum.app.data.dao
import androidx.room.*
import com.animeforum.app.data.model.User
import kotlinx.coroutines.flow.Flow
@Dao interface UserDao{ @Query("SELECT * FROM users") fun getAllUsers():Flow<List<User>>; @Query("SELECT * FROM users WHERE username=:username") suspend fun getUserByUsername(username:String):User?; @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun insertUser(user:User); @Update suspend fun updateUser(user:User); @Delete suspend fun deleteUser(user:User) }