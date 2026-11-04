package com.example.biblesearchum.network

import com.example.biblesearchum.model.BibleBook
import com.example.biblesearchum.model.BibleVerse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BibleApiService {
    @GET("get-books/{translation}/")
    suspend fun getBooks(@Path("translation") translation: String): List<BibleBook>

    @GET("get-verse/{translation}/{bookId}/{chapter}/{verse}/")
    suspend fun getVerse(
        @Path("translation") translation: String,
        @Path("bookId") bookId: Int,
        @Path("chapter") chapter: Int,
        @Path("verse") verse: Int
    ): BibleVerse
}

object RetrofitInstance {
    private const val BASE_URL = "https://bolls.life/"

    val api: BibleApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BibleApiService::class.java)
    }
}