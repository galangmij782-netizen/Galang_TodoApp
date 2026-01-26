package com.example.todos.data.repository

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User
import com.example.todos.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class to handle data operations
 * Acts as a single source of truth for user and todo data
 * Separates the data layer from the UI layer
 */
class UserRepository {

    // API service instance from RetrofitClient
    private val apiService = RetrofitClient.apiService

    /**
     * Fetches users from the API
     * Uses withContext(Dispatchers.IO) to ensure network call happens on IO thread
     *
     * @return Result<List<User>> - Success with user list or Failure with exception
     */
    suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUsers()

                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        Result.success(users)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Fetches todos for a specific user
     * Uses withContext(Dispatchers.IO) to ensure network call happens on IO thread
     *
     * @param userId ID of the user to fetch todos for
     * @return Result<List<Todo>> - Success with todo list or Failure with exception
     */
    suspend fun getTodosByUser(userId: Int): Result<List<Todo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTodosByUser(userId)

                if (response.isSuccessful) {
                    val todos = response.body()
                    if (todos != null) {
                        Result.success(todos)
                    } else {
                        Result.failure(Exception("Empty response body"))
                    }
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}