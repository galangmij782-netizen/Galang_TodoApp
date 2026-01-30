package com.example.todos.data.remote

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for JSONPlaceholder endpoints
 */
interface ApiService {

    /**
     * Fetches all users from the /users endpoint
     * @return Response containing a list of User objects
     */
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    /**
     * Fetches todos for a specific user
     * @param userId The ID of the user to fetch todos for
     * @return Response containing a list of Todo objects
     */
    @GET("todos")
    suspend fun getTodosByUser(@Query("userId") userId: Int): Response<List<Todo>>
}package com.example.todos.data.remote

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for JSONPlaceholder endpoints
 */
interface ApiService {

    /**
     * Fetches all users from the /users endpoint
     * @return Response containing a list of User objects
     */
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    /**
     * Fetches todos for a specific user
     * @param userId The ID of the user to fetch todos for
     * @return Response containing a list of Todo objects
     */
    @GET("todos")
    suspend fun getTodosByUser(@Query("userId") userId: Int): Response<List<Todo>>
}