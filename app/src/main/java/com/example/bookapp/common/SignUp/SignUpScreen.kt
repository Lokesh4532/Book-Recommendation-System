package com.example.bookapp.common.SignUp


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
fun SignUpScreen(navController: NavController) {

    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var confirm by remember {
        mutableStateOf("")
    }
    var selectedRole by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) } // Dropdown state

    val roles = listOf("Buyer", "Seller")

    val viewModel: SignUpViewModel = hiltViewModel()
    val imageResources = listOf(
        R.drawable.book1,
    )

    var currentImageIndex by remember { mutableIntStateOf(0) }

    // LaunchedEffect to change the image index every few seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(6000) // Change image every 3 seconds
            currentImageIndex = (currentImageIndex + 1) % imageResources.size
        }
    }

    // Background image to match the food delivery theme
    val backgroundImage: Painter = painterResource(id = imageResources[currentImageIndex])
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = R.drawable.book1), contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f),
            contentScale = ContentScale.Crop
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
                text="Sign up",
                modifier = Modifier,
                color = Color.White,
                fontFamily= FontFamily.Cursive,
                fontSize = 79.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Just a few quick things " ,
                fontFamily = FontFamily.Cursive,
                color = Color.White,
                fontSize = 28.sp,)
            Text("to get started !" ,
                fontFamily = FontFamily.Cursive,
                color = Color.White,
                fontSize = 28.sp,)

            Spacer(modifier = Modifier.height(19.dp))
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Toggle password visibility",
                        tint = Color.DarkGray
                    )
                },
                value = email,
                onValueChange = { email = it },
                label = { Text("Email",
                    color = Color.White,
                    fontFamily = FontFamily.Cursive) },
                modifier = Modifier
                    .fillMaxWidth()//  // Adjust the width as needed
                    .background(Color.White.copy(0.2f), shape = RoundedCornerShape(32.dp)),
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
                // isError = password.isNotEmpty() && confirm.isNotEmpty() && password != confirm,
                label = { Text("Password",
                    color=Color.White,
                    fontFamily= FontFamily.Cursive
                ) },
                modifier = Modifier
                    .fillMaxWidth()//  // Adjust the width as needed
                    .background(Color.White.copy(0.2f), shape = RoundedCornerShape(22.dp)),
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
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Toggle password visibility",
                        tint = Color.DarkGray
                    )
                },
                value = confirm,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { confirm = it },
                label = { Text("Confirm Password",
                    color = Color.White,
                    fontFamily = FontFamily.Cursive,
                ) },
                modifier = Modifier
                    .fillMaxWidth()//  // Adjust the width as needed
                    .background(Color.White.copy(0.2f), shape = RoundedCornerShape(22.dp)),
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
                    viewModel.signUp(email, password,
                        onSuccess = {
                            navController.navigate("addedBook")
                            Toast.makeText(context, "SIGN UP SUCCESS", Toast.LENGTH_SHORT).show()
                        },
                        onError = {
                            Toast.makeText(context, "SIGN UP FAILED", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier
                    .width(300.dp)// //
                    .padding(16.dp),
                enabled =  email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && password == confirm ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0d98ba).copy(alpha= 0.65f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create account")
            }

            Box(
                contentAlignment = Alignment.Center,
            ) {
                if(viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            Row {
                Text(
                    "Already have an account?",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Cursive
                )
                Text(
                    " Log In",
                    modifier = Modifier.clickable {
                        navController.navigate("signIn")
                    },
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 20.sp,
                    color = Color(0xFF239ED0)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
}