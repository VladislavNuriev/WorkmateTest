package com.example.workmatetest.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.generateuser.ui.GenerateUserScreen
import com.example.profile.ui.UserProfileScreen
import com.example.users.UsersScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.GenerateUser.route
    ) {
        composable(Screen.GenerateUser.route) {
            GenerateUserScreen (
                navigateToUsers = {
                    navController.navigate(Screen.Users.route)
                }
            )
        }
        composable(Screen.Users.route) {
            UsersScreen (
                onUserClick = { user ->
                    navController.navigate(Screen.Profile.createRoute(user.id.toString()))
                },
                onAddUserClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Profile.route) {
            val userId = Screen.Profile.getUserId(it.arguments).toInt()
            UserProfileScreen(
                userId = userId,
                onFinished = { navController.popBackStack() },
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object GenerateUser : Screen("generate_user")
    data object Users : Screen("users")

    data object Profile : Screen("profile/{user_id}") {
        fun createRoute(userId: String): String {
            return "profile/$userId"
        }
        fun getUserId(arguments: Bundle?): String {
            return arguments?.getString("user_id") ?: ""
        }
    }

}