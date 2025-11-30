package com.animeforum.app.data.database
import android.content.Context
import androidx.room.*
import com.animeforum.app.data.dao.PostDao
import com.animeforum.app.data.dao.UserDao
import com.animeforum.app.data.model.Post
import com.animeforum.app.data.model.User
@Database(entities=[Post::class, User::class], version=2, exportSchema=false)
@TypeConverters(Converters::class)
abstract class AnimeForumDatabase: RoomDatabase(){
  abstract fun postDao(): PostDao
  abstract fun userDao(): UserDao
  companion object{
    @Volatile private var INSTANCE: AnimeForumDatabase? = null
    fun getDatabase(context: Context): AnimeForumDatabase = INSTANCE ?: synchronized(this){
      INSTANCE ?: Room.databaseBuilder(context.applicationContext, AnimeForumDatabase::class.java, "anime_forum_database").fallbackToDestructiveMigration().build().also{ INSTANCE = it }
    }
  }
}
