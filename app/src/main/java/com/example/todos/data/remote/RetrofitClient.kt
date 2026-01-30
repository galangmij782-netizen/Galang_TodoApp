package com.example.todos.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object to provide Retrofit instance
 */
object RetrofitClient {

    // Base URL for JSONPlaceholder API
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * Creates an OkHttpClient with logging interceptor for debugging
     * and timeout configurations
     */
    private val okHttpClient: OkHttpClient by lazy {

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS) // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS) // Write timeout
            .build()
    }

    /**
     * Lazy initialization of Retrofit instance
     * Uses Gson converter for JSON parsing
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set base URL
            .client(okHttpClient) // Set custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
            .build()
    }

    /**
     * Provides the ApiService implementation
     * This is the main entry point for making API calls
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}