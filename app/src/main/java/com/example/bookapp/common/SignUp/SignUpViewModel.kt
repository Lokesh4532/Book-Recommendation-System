package com.example.bookapp.common.SignUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var isLoading by mutableStateOf(false)
    // avoids the value can't be  modified outside the class but can only be read

    //custom email authentication logic
    fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            isLoading=true
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading=false
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError()
                    }
                }
        }
    }

}