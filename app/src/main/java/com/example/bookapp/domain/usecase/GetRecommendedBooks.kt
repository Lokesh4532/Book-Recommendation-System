package com.example.bookapp.domain.usecase

import com.example.bookapp.domain.model.Book
import com.example.bookapp.domain.repository.BookRepository
import com.example.bookapp.utils.ApiResponse


class GetRecommendedBooksUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(userBooks: List<com.example.bookapp.common.Book>): ApiResponse<List<com.example.bookapp.common.Book>> {
        return repository.getRecommendations(userBooks)
    }
}
