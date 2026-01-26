package com.example.todos.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import kotlinx.coroutines.delay

/**
 * Main screen with smooth transitions
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "userList",
        enterTransition = {
            fadeIn(animationSpec = tween(600)) +
                    slideInHorizontally(
                        animationSpec = tween(600),
                        initialOffsetX = { it }
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400)) +
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { -it }
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(600)) +
                    slideInHorizontally(
                        animationSpec = tween(600),
                        initialOffsetX = { -it }
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400)) +
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { it }
                    )
        }
    ) {
        composable("userList") {
            UserListScreen(navController = navController)
        }

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
 * User List Screen with smooth loading and transitions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    var isInitialLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    // Add minimum loading time for initial load only
    LaunchedEffect(userState) {
        when (userState) {
            is UserState.Loading -> {
                if (isInitialLoading) {
                    delay(500)
                    isInitialLoading = false
                }
            }
            is UserState.Success,
            is UserState.Error -> {
                isRefreshing = false
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Todo Users",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Select a user to view their todos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    when (userState) {
                        is UserState.Success -> {
                            IconButton(onClick = {
                                isRefreshing = true
                                viewModel.retry()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh users",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        else -> {}
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Check both initial loading AND if we're refreshing
            if ((isInitialLoading && userState is UserState.Loading) || isRefreshing) {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    message = if (isRefreshing) "Refreshing..." else "Loading users..."
                )
            } else {
                AnimatedContent(
                    targetState = userState,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(600)) togetherWith
                                fadeOut(animationSpec = tween(400))
                    },
                    label = "user_content"
                ) { state ->
                    when (state) {
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
                                onRetry = {
                                    isRefreshing = true
                                    viewModel.retry()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {} // Loading handled above
                    }
                }
            }
        }
    }
}

/**
 * User list content with staggered animations
 */
@Composable
fun UserListContent(
    users: List<com.example.todos.data.model.User>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Animated user count info
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.large,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = "${users.size} Users",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Tap any user to see their tasks",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // User list with staggered animation
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users, key = { it.id }) { user ->
                var isVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(users.indexOf(user) * 50L) // Stagger animation
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInVertically(
                                animationSpec = tween(400),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    UserItem(
                        user = user,
                        onClick = {
                            navController.navigate("todos/${user.id}")
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}