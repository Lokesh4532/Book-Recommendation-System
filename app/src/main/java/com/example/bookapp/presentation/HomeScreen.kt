package com.example.bookapp.presentation


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookapp.R
import com.example.bookapp.common.Book
import com.example.bookapp.common.bottomnav.BottomBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(navController: NavController) {

    val viewModel: BookViewModel = hiltViewModel()
    val context = LocalContext.current
    val userBooks by viewModel.userBooks.collectAsState()
    val recommendedBooks by viewModel.recommendedBooks.collectAsState()


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
                        label = { Text("Get Recommended Books...", color = Color.White) },
                        textStyle = TextStyle(color = Color.White),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Red,
                                modifier = Modifier.clickable {
                                    if (userBooks.isNotEmpty()) {
                                        viewModel.getRecommendationsInternal(userBooks)
                                    }
                                }
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
                    items(recommendedBooks) { product ->
                        BooksItem1(product = product)
                    }
                }
            }
        }
    }
}

@Composable
fun BooksItem1(product: Book) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f)) // Transparent background for the whole column
                .padding(16.dp)
                .clip(RoundedCornerShape(29.dp)) // Optional: to give rounded corners
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter= painterResource(R.drawable.book3),
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
                        color= Color(0xFF73c2fb),
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
