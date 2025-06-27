package com.example.bookapp.common.SignIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookapp.R


import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }
    val viewModel: SignInViewModel = hiltViewModel()

    val imageResources = listOf(
        R.drawable.book1,
    )

    // State to hold the current image index
    var currentImageIndex by remember { mutableIntStateOf(0) }

    // LaunchedEffect to change the image index every few seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(7000) // Change image every 3 seconds
            currentImageIndex = (currentImageIndex + 1) % imageResources.size
        }
    }

    // Background image to match the food delivery theme
    val backgroundImage: Painter = painterResource(id = imageResources[currentImageIndex])
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        // Background image to match the food delivery theme
        Image(
            painter = painterResource(id = R.drawable.book1), // Replace with your background image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f)) // Adjust alpha for desired transparency
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Text(
                "Login",
                modifier = Modifier,
                fontSize = 79.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Hello, welcome back to", fontSize = 28.sp, color = Color.White, fontFamily = FontFamily.Cursive)
            Text("Paper Hub", fontSize = 28.sp, color = Color.White, fontFamily = FontFamily.Cursive)
            Spacer(modifier = Modifier.height(19.dp))

            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Toggle password visibility",
                        tint = Color.DarkGray
                    )
                },
                value = email,
                onValueChange = { email = it },
                label = { Text(text="Username",
                    color = Color.White,
                    fontFamily = FontFamily.Cursive
                ) },
                modifier = Modifier
                    .fillMaxWidth()// Adjust the width as needed
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(22.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Ensure the background color is applied from Modifier.background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp) // Ensure the shape is applied
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Toggle password visibility",
                        tint = Color.DarkGray
                    )
                },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { password = it },
                label = { Text(text="Password",
                    color = Color.White,
                    fontFamily = FontFamily.Cursive
                )
                },
                modifier = Modifier
                    .fillMaxWidth() // Adjust the width as needed
                    .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(22.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Ensure the background color is applied from Modifier.background
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(42.dp)// Ensure the shape is applied
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    viewModel.signIn(email, password,
                        onSuccess = {
                            Toast.makeText(context, "SIGN IN SUCCESS", Toast.LENGTH_SHORT).show()
                            navController.navigate("addedBook")

                        },
                        onError = {
                            Toast.makeText(context, "SIGN IN FAILED", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0d98ba).copy(alpha= 0.65f), // Set button color
                    contentColor = Color.White // Set text color
                ),
                shape = RoundedCornerShape(12.dp) // Ensure button has rounded corners
            ) {
                Text("Log in") // Button text
            }
            Row {
                Text(
                    "Doesn't have a account?",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Cursive
                )
                Text(
                    " Sign Up",
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier.clickable {
                        navController.navigate("signUp")
                    },
                    fontSize = 20.sp,
                    color = Color(0xFF0d98ba)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    val navController= rememberNavController()
    SignInScreen(navController)
}