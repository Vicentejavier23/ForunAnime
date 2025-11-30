package com.animeforum.app
import android.app.Application
import com.animeforum.app.data.database.AnimeForumDatabase
class AnimeForumApplication: Application(){ val database by lazy{ AnimeForumDatabase.getDatabase(this) } }