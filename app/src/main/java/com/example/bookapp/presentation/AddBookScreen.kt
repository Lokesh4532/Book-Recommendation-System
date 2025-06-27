package com.example.bookapp.presentation





import android.Manifest
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookapp.R
import com.example.bookapp.common.Book
import com.example.bookapp.common.bottomnav.BottomBar
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddBookScreen(navController: NavController){
    val context= LocalContext.current
    val viewModel: BookViewModel = hiltViewModel()
    var title by remember {
        mutableStateOf("")
    }
    var genre by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var rate by remember {
        mutableStateOf("")
    }
    var id by remember {
        mutableStateOf("")
    }
    var url by remember {
        mutableStateOf("")
    }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userId= auth.currentUser?.uid
    var restaurantFood by remember {
        mutableStateOf<Uri?>(null)
    }
    val bookItems = Book(
        id = userId.toString(), // This will be generated in the `addProductToSeller` method
        title = title,
        genre = genre,
        rate=rate,
        imageUrl = restaurantFood.toString(),
        description = description
    )
    val chooserDialogFood = remember {
        mutableStateOf(false)
    }

    val cameraImageUriFood = remember {
        mutableStateOf<Uri?>(null)
    }
    val cameraImageLauncherFood = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            restaurantFood = cameraImageUriFood.value // Use this for restaurant image
        }
    }
    val restaurantFoodLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            restaurantFood = it // Use this for restaurant menu
        }
    }
    fun createImageUriFood(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = ContextCompat.getExternalFilesDirs(
            navController.context, Environment.DIRECTORY_PICTURES
        ).first()
        return FileProvider.getUriForFile(navController.context,
            "${navController.context.packageName}.provider",
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                cameraImageUriFood.value = Uri.fromFile(this)
            })
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraImageLauncherFood.launch(createImageUriFood())
            }
        }

    @Composable
    fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
        AlertDialog(onDismissRequest = {
            chooserDialogFood.value = false
        },
            confirmButton = {
                TextButton(onClick = onCameraSelected) {
                    Text(text = "Camera")
                }
            },
            dismissButton = {
                TextButton(onClick = onGallerySelected) {
                    Text(text = "Gallery")
                }
            },
            title = { Text(text = "Select your source?") },
            text = { Text(text = "Would you like to pick an image from the gallery or use the camera") })
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->

        Column(modifier = Modifier.fillMaxSize()
            .padding(paddingValues)) {
            if (chooserDialogFood.value) {
                ContentSelectionDialog(onCameraSelected = {
                    chooserDialogFood.value = false
                    if (navController.context.checkSelfPermission(Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        cameraImageLauncherFood.launch(createImageUriFood())
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }, onGallerySelected = {
                    chooserDialogFood.value = false
                    restaurantFoodLauncher.launch("image/*")
                })
            }

            Row(
                modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Add Book",
                    fontSize = 38.sp,  // Slightly larger for a bold heading
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Cursive,// Stronger emphasis
                    color = Color.Black,  // Ensuring good contrast
                    //textAlign = TextAlign.Center,  // Centering the text
                    modifier = Modifier
                        .fillMaxWidth()  // Makes sure it spans the width for better alignment
                    //.padding(top = 16.dp, bottom = 8.dp)  // Adds spacing around the text
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(Color(0xFFF6F6F6)) // Light gray background
            ) {

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, color = Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Box(modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center) {
                                if (restaurantFood != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(restaurantFood),
                                        contentDescription = "Selected Image",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.Gray, CircleShape)
                                    )
                                } else {
                                    IconButton(
                                        onClick = { chooserDialogFood.value = true },
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape)
                                            .background(Color.Gray.copy(alpha = 0.2f))
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.camera),
                                            contentDescription = "Add Photo",
                                            //tint = Color.DarkGray,
                                            modifier = Modifier.size(80.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Add Book",
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily.Cursive,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = "\u002A",
                                    fontSize = 18.sp,
                                    color = Color.Red.copy(alpha=0.7f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "All fields are necessary ",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                // Basic Details Section
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                color = Color.Black.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Book Details",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Please give the details of the book.",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                //keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                label = { Text("Book Name *") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = genre,
                                onValueChange = { genre = it },
                                // keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                label = { Text("Book Type *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = "\u002A",
                                    fontSize = 18.sp,
                                    color = Color.Red.copy(alpha=0.7f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "All fields are necessary ",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Owner Contact Details Section
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                color = Color.Black.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Description And Ratings",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Please give a short description of the Book",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                //  keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                label = { Text("Description *") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedTextField(
                                value = rate,
                                onValueChange = { rate = it },
                                // keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                label = { Text("Ratings *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = "\u002A",
                                    fontSize = 18.sp,
                                    color = Color.Red.copy(alpha=0.7f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "All fields are necessary ",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    androidx.compose.material3.Button(
                        onClick = {
                            viewModel.uploadBook(
                                restaurantFood,bookItems,onSuccess = {
                                title = ""
                                description=""
                                genre=""
                                rate=""
                                restaurantFood= null
                                Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show()
                            },onError = {
                                Toast.makeText(context,"Error Adding",Toast.LENGTH_SHORT).show()
                            }, context = context)
                        },
                        enabled = title.isNotEmpty() && description.isNotEmpty() && rate.isNotEmpty() && genre.isNotEmpty() && restaurantFood!=null,
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF5722)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Add Details",
                            color = Color.White
                        )
                    }
                }

                // Help Footer
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "If you need any help, check out the FAQs",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                }
            }

        }
    }

}


