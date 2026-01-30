package com.example.todos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todos.domain.UserState
import com.example.todos.ui.viewmodel.UserViewModel

/**
 * Main screen with navigation
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "userList"
    ) {
        // User List Screen
        composable("userList") {
            UserListScreen(navController = navController)
        }

        // Todo Screen with parameters
        composable("todos/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            TodoScreen(
                userId = userId,
                navController = navController
            )
        }
    }
}

/**
 * User List Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JSONPlaceholder Users",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = userState) {
                is UserState.Loading -> {
                    LoadingScreen(
                        message = "Loading users...",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UserState.Success -> {
                    UserListContent(
                        users = state.users,
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UserState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.retry() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * User list content
 */
@Composable
fun UserListContent(
    users: List<com.example.todos.data.model.User>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(users, key = { it.id }) { user ->
            UserItem(
                user = user,
                onClick = {
                    // Navigate to Todo Screen with user ID
                    navController.navigate("todos/${user.id}")
                }
            )
        }
    }
}