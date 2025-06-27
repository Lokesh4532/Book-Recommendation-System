package com.example.bookapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.chatbot.ChatPage
import com.example.bookapp.common.SignIn.SignInScreen
import com.example.bookapp.common.SignUp.SignUpScreen

@Composable
fun AppNavHost() {
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = "signIn") {
        composable("addBookScreen") {
            AddBookScreen(navController)
        }
        composable("signIn"){
            SignInScreen(navController)
        }
        composable("signUp"){
            SignUpScreen(navController)
        }
        composable("addedBook"){
            AddedBookScreen(navController)
        }
        composable("aiScreen"){
            BookScreen(navController)
        }
        composable("chat"){
            ChatPage(navController)
        }

    }
}
