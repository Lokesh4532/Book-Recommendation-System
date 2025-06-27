package com.example.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.chatbot.ChatPage
import com.example.bookapp.common.SignIn.SignInScreen
import com.example.bookapp.common.SignUp.SignUpScreen
import com.example.bookapp.presentation.AppNavHost
import com.example.bookapp.presentation.MyApp
import com.example.bookapp.ui.theme.BookAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            //ChatPage(navController)
            //MyApp()
            AppNavHost()
            //SignUpScreen(navController)
        }
    }
}