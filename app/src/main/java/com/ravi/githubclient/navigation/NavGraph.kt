package com.ravi.githubclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ravi.githubclient.screens.DetailsScreen
import com.ravi.githubclient.screens.HomeScreen
import com.ravi.githubclient.screens.WebViewScreen

@Composable
fun NavGraph(startDestination: String = "home") {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { HomeScreen(navController) }
        composable("details/{owner}/{repoName}") { backStackEntry ->
            DetailsScreen(
                owner = backStackEntry.arguments?.getString("owner") ?: "",
                repoName = backStackEntry.arguments?.getString("repoName") ?: "",
                navController = navController
            )
        }
        composable("webview/{url}") { backStackEntry ->
            WebViewScreen(url = backStackEntry.arguments?.getString("url") ?: "")
        }
    }
}
