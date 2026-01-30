package com.example.todos.data.repository

import com.example.todos.data.model.Todo
import com.example.todos.data.model.User
import com.example.todos.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Repository class to handle data operations
 */
class UserRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                // Add delay for loading visibility (300ms as requested)
                delay(300)

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

    suspend fun getTodosByUser(userId: Int): Result<List<Todo>> {
        return withContext(Dispatchers.IO) {
            try {
                // Add delay for loading visibility (500ms as requested for retry)
                delay(500)

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