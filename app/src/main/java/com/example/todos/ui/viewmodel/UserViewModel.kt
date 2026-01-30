package com.example.todos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.data.repository.UserRepository
import com.example.todos.domain.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.net.SocketTimeoutException

/**
 * ViewModel for managing user list UI state
 */
class UserViewModel : ViewModel() {

    private val repository = UserRepository()
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    // Track if it's initial load
    private var isInitialLoad = true

    // Automatically load users when ViewModel is created
    init {
        loadUsers()
    }

    /**
     * Loads users from the repository
     */
    fun loadUsers() {
        viewModelScope.launch {
            _userState.value = UserState.Loading

            val result = repository.getUsers()

            result.onSuccess { users ->
                _userState.value = UserState.Success(users)
                isInitialLoad = false
            }.onFailure { exception ->
                val errorMessage = when (exception) {
                    is UnknownHostException -> {
                        "No internet connection. Please check your network settings."
                    }
                    is SocketTimeoutException -> {
                        "Connection timeout. Please try again."
                    }
                    else -> {
                        exception.message ?: "An unknown error occurred"
                    }
                }
                _userState.value = UserState.Error(errorMessage)
                isInitialLoad = false
            }
        }
    }

    /**
     * Retry function to reload users after an error
     */
    fun retry() {
        loadUsers()
    }
}