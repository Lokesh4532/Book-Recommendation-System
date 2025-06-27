package com.example.bookapp.data.repository



import com.example.bookapp.domain.model.Book
import com.example.bookapp.domain.repository.BookRepository
import com.example.bookapp.utils.ApiResponse
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.serialization.json.Json

class BookRepositoryImpl(private val model: GenerativeModel) : BookRepository {

    override suspend fun getRecommendations(userBooks: List<com.example.bookapp.common.Book>): ApiResponse<List<com.example.bookapp.common.Book>> {
        val booksInfo = userBooks.joinToString(", ") {
            """
                Title: ${it.title}, 
                Genre: ${it.genre}, 
                Rate: ${it.rate}, 
                Description: ${it.description}
            """.trimIndent()
        }
        val prompt = """
            I have read these books: $booksInfo. Return the result in JSON format only.
            The response must be a JSON array like Example output: [{"id":"01","title": "Book1", "rate":"4.5","genre": "Fiction", "imageUrl": "url1","description":"There is a story of boy named XYZ"}].
            No extra text, rate of out 5 and give small description of 1 line just JSON. dont add same book let the user first add book Recommend similar books from Libris Cover Image API, Open Library Covers API, Bookcover-API by w3slley, ISBNdb API book any 6 with valid image urls and also check whethet the image url is of book or not before giving use real image url.
        """.trimIndent()

        val response = model.generateContent(prompt)

        println("Raw Response: ${response.text}") // Log the raw response for debugging

        response.text?.let { jsonResponse ->
            val jsonArrayRegex = """\[[\s\S]*]""".toRegex() // Matches any JSON array
            val matchResult = jsonArrayRegex.find(jsonResponse)

            return matchResult?.value?.let {
                try {
                    val books = Json.decodeFromString<List<com.example.bookapp.common.Book>>(it)
                    ApiResponse(success = true, message = "Books fetched successfully", data = books)
                } catch (e: Exception) {
                    println("JSON Parsing Error: ${e.message}") // Log the error
                    ApiResponse(success = false, message = "Invalid JSON format", data = emptyList())
                }
            } ?: ApiResponse(success = false, message = "No valid JSON response", data = emptyList())
        } ?: return ApiResponse(success = false, message = "Empty response from model", data = emptyList())
    }
}
