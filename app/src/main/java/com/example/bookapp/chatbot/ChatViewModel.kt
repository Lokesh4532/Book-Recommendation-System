package com.example.bookapp.chatbot
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question : String){
        viewModelScope.launch {
            try{
                val systemPrompt = content{
                    text("""
                    You are a chatbot assistant that helps users understand how this BookApp works.
                    
                    App Features:
                    - Users can sign up and log in securely.
                    - They can search for books by title, author, or genre.
                    - Each book shows a rating, description, and cover image.
                    - Users receive book recommendations based on interest.
                    - They can also add books to a wishlist.
                    - The app uses Firebase for backend operations like authentication and real-time data sync.
                    
                    The App works when the user login after that the book collections is on the heart icon and for adding 
                    book press + button and for recommended book press the book icon and for having any issues related to the 
                    app press bot icon.

                    U can answer any questions other than a book app related questions also any general questions
                """.trimIndent())
                }
                val chat = generativeModel.startChat(
                    history = listOf(systemPrompt) + messageList.map {
                        content { text(it.message) }
                    }
                )

                messageList.add(MessageModel(question,"user"))
                messageList.add(MessageModel("Typing....","model"))

                val response = chat.sendMessage(question)
                messageList.removeAt(messageList.size - 1)
                messageList.add(MessageModel(response.text.toString(),"model"))
            }catch (e : Exception){
                messageList.removeAt(messageList.size - 1)
                messageList.add(MessageModel("Error : "+e.message.toString(),"model"))
            }


        }
    }
}