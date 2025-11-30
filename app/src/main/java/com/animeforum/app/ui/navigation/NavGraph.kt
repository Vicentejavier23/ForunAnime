package com.animeforum.app.ui.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.animeforum.app.ui.screens.*
import com.animeforum.app.viewmodel.PostViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreatePost : Screen("create_post?postId={postId}") {
        fun createRoute(id: Int? = null) = id?.let { "create_post?postId=$it" } ?: "create_post"
    }

    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(id: Int) = "post_detail/$id"
    }
}
@Composable
fun NavGraph(navController: NavHostController, vm: PostViewModel) {
    NavHost(navController, Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = vm,
                onNavigateToCreate = { navController.navigate(Screen.CreatePost.createRoute()) },
                onNavigateToDetail = { id -> navController.navigate(Screen.PostDetail.createRoute(id)) },
                onNavigateToEdit = { id -> navController.navigate(Screen.CreatePost.createRoute(id)) }
            )
        }

        composable(
            route = Screen.CreatePost.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStack ->
            val postId = backStack.arguments?.getInt("postId")?.takeIf { it != -1 }
            CreatePostScreen(vm, postId) { navController.popBackStack() }
        }

        composable(Screen.PostDetail.route) { backStack ->
            backStack.arguments?.getString("postId")?.toIntOrNull()?.let {
                PostDetailScreen(
                    postId = it,
                    viewModel = vm,
                    onNavigateBack = { navController.popBackStack() }
                ) { id ->
                    navController.navigate(Screen.CreatePost.createRoute(id))
                }
            }
        }
    }
}