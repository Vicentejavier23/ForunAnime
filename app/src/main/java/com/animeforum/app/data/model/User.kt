package com.animeforum.app.data.model
import androidx.room.*
@Entity(tableName="users")
data class User(@PrimaryKey val username:String, val email:String, val avatarUrl:String="", val joinedDate:Long=System.currentTimeMillis())
