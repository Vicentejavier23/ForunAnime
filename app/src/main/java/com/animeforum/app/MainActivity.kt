package com.animeforum.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.animeforum.app.ui.navigation.NavGraph
import com.animeforum.app.viewmodel.PostViewModel
import com.animeforum.app.viewmodel.PostViewModelFactory
class MainActivity: ComponentActivity(){
  private val vm: PostViewModel by viewModels{
    val app = application as AnimeForumApplication
    PostViewModelFactory(app.database.postDao())
  }
  override fun onCreate(savedInstanceState: Bundle?){ super.onCreate(savedInstanceState)
    setContent{ Surface(color = MaterialTheme.colorScheme.background){ val nav=rememberNavController(); NavGraph(nav, vm) } }
  }
}
