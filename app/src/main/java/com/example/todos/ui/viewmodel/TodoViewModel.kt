package com.example.todos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.data.repository.UserRepository
import com.example.todos.domain.TodoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.net.SocketTimeoutException

class TodoViewModel : ViewModel() {
    private val repository = UserRepository()
    private val _todoState = MutableStateFlow<TodoState>(TodoState.Loading)
    val todoState: StateFlow<TodoState> = _todoState.asStateFlow()

    // Store current user ID for retry
    private var currentUserId: Int? = null

    fun loadTodos(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            _todoState.value = TodoState.Loading
            val result = repository.getTodosByUser(userId)
            result.onSuccess { todos ->
                _todoState.value = TodoState.Success(todos)
            }.onFailure { exception ->
                val errorMessage = when (exception) {
                    is UnknownHostException -> {
                        "No internet connection. Please check your network settings."
                    }
                    is SocketTimeoutException -> {
                        "Connection timeout. Please try again."
                    }
                    else -> {
                        exception.message ?: "Failed to load todos. Please try again."
                    }
                }
                _todoState.value = TodoState.Error(errorMessage)
            }
        }
    }

    fun retry() {
        currentUserId?.let { userId ->
            loadTodos(userId)
        } ?: run {
            _todoState.value = TodoState.Error("No user selected. Please go back and try again.")
        }
    }
}