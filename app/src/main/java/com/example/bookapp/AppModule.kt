package com.example.bookapp

import com.example.bookapp.data.repository.BookRepositoryImpl
import com.example.bookapp.domain.repository.BookRepository
import com.example.bookapp.domain.usecase.GetRecommendedBooksUseCase
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGeminiApi(): GenerativeModel {
        return GenerativeModel(modelName = "gemini-1.5-flash", apiKey = BuildConfig.apiKey)
    }

    @Provides
    @Singleton
    fun provideBookRepository(model: GenerativeModel): BookRepository {
        return BookRepositoryImpl(model)
    }

    @Provides
    fun provideGetRecommendedBooksUseCase(repository: BookRepository): GetRecommendedBooksUseCase {
        return GetRecommendedBooksUseCase(repository)
    }
}
