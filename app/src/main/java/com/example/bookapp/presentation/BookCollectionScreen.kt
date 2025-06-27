package com.example.bookapp.presentation

import VoiceRecognizer
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookapp.R
import com.example.bookapp.common.Book
import com.example.bookapp.common.bottomnav.BottomBar
import android.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddedBookScreen(navController: NavController) {

    val viewModel: BookViewModel = hiltViewModel()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val book = viewModel.userBooks.collectAsState()

    var spokenText by remember { mutableStateOf("") }
    val voiceRecognizer = remember {
        VoiceRecognizer(context) { result ->
            spokenText = result

            when {
                result.contains("add book", ignoreCase = true) -> {
                    navController.navigate("addBookScreen")
                }
                result.contains("open chatbot", ignoreCase = true) -> {
                    navController.navigate("chat")
                }
                result.contains("recommended book", ignoreCase = true) -> {
                    navController.navigate("aiScreen")
                }
            }
        }
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var productId by remember {
        mutableStateOf("")
    }
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize() ,
            contentAlignment = Alignment.Center
        ) {
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
                    .background(Color.Black.copy(alpha = 0.55f)) // Adjust alpha for desired transparency
            )
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)) // Additional overlay
            ) {
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDeleteDialog=false
                        },
                        text = {
                            Text(
                                text = "Are you sure to delete your product ?",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.deleteProduct(productId, onSuccess = {
                                        Toast.makeText(context,"Successfully deleted", Toast.LENGTH_SHORT).show()
                                       showDeleteDialog=false
                                    }, onError = {
                                        Toast.makeText(context,"Error Occurred", Toast.LENGTH_SHORT).show()
                                    })
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red.copy(alpha = 0.8f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(48.dp)
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Delete")
                            }
                        },
                        containerColor = Color.White,
                        shape = RoundedCornerShape(16.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(WindowInsets.statusBars.asPaddingValues())// Adjust padding as needed
                        .background(color = Color.White.copy(alpha = 0.1f))
                        .clip(RoundedCornerShape(8.dp)) // Clips the Box to match the border shape
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* Handle text change */ },
                        label = { Text("See Your Books...", color = Color.White) },
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                modifier = Modifier.clickable {
                                    voiceRecognizer.startListening()
                                },
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            cursorColor = Color.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1), // Two products per row
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .background(Color.Black.copy(alpha = 0.1f))
                ) {
                    items(book.value) { product ->
                        BooksItem(product = product, onEditClick = {
                          showDeleteDialog=true
                            productId=product.id
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun BooksItem(product: Book , onEditClick: (Book) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { onEditClick(product) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Edit Product",
                tint = Color.Red
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f)) // Transparent background for the whole column
                .padding(16.dp)
                .clip(RoundedCornerShape(29.dp)) // Optional: to give rounded corners
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = product.rate,
                            fontSize = 14.sp,
                            color = Color(0xFFFFD700),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Genre: ${product.genre}",
                        color=Color(0xFF73c2fb),
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Description: ${product.description}",
                        color=Color(0xFF73c2fb),
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(onClick = { /* Add to favorites action */ }) {
                    Text("Add to Favourite", color = Color.White)
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}