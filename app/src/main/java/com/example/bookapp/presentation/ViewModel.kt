package com.example.bookapp.presentation


import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookapp.domain.model.Book
import com.example.bookapp.domain.usecase.GetRecommendedBooksUseCase
import com.example.bookapp.supabase.SupabaseStorageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(private val getRecommendedBooksUseCase: GetRecommendedBooksUseCase) : ViewModel() {

    private val dbReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private val _userBooks = MutableStateFlow<List<com.example.bookapp.common.Book>>(emptyList())
    val userBooks= _userBooks.asStateFlow()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    private val _recommendedBooks = MutableStateFlow<List<com.example.bookapp.common.Book>>(emptyList())
    val recommendedBooks: StateFlow<List<com.example.bookapp.common.Book>> = _recommendedBooks
    private var productListener: ValueEventListener? = null


    init{
        ListeningToProducts()

//        viewModelScope.launch {
//            userBooks.collect { books ->
//                // Trigger recommendation update when userBooks changes
//                getRecommendationsInternal(books)
//            }
//        }
    }

    fun ListeningToProducts() {
        if (userId != null && productListener == null) {
            productListener = dbReference.child("books").child(userId).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<com.example.bookapp.common.Book>()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(com.example.bookapp.common.Book::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    _userBooks.value = productList  // Update LiveData with new product list
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    fun getRecommendationsInternal(books: List<com.example.bookapp.common.Book>) {
        viewModelScope.launch {
            val response = getRecommendedBooksUseCase(books)
            if (response.success) {
                _recommendedBooks.value = response.data
            }
        }
    }

    // Optional if you still want manual triggering
    fun getRecommendationsManually() {
        getRecommendationsInternal(userBooks.value)
    }


    fun uploadBook(
        bookImage: Uri?,
        book: com.example.bookapp.common.Book,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        context: Context
    ) {
        if (bookImage!= null ) {
            viewModelScope.launch {
                val storageUtils = SupabaseStorageUtils(context)
                val bookImage = storageUtils.uploadImage(bookImage)
                val Image = book.copy(imageUrl = bookImage.toString())
                addBook(Image, onSuccess, onError)
            }
        }
    }


    fun addBook(product: com.example.bookapp.common.Book, onSuccess:()-> Unit, onError:(String)-> Unit) {
        // Get the authenticated user's UID
        if (userId != null) {
            val bookId = dbReference.child("books").child(userId).push().key
            val updatedBook = product.copy(id = bookId.toString())
            if (bookId != null) {
                dbReference.child("books").child(userId).child(bookId).setValue(updatedBook)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onError("Error Occurred")
                            // Handle failure
                        }
                    }
            }
        }
    }

    fun deleteProduct(productId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (userId != null) {
            dbReference.child("books").child(userId).child(productId).removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("Error Occurred while deleting the product")
                    }
                }
        }
    }

}

