package com.example.api

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Import viewModel
// Import lớp postItem của bạn (lớp đại diện cho một task item)
import com.example.api.Model.data.postItem
import com.example.api.ui.theme.ApiTheme
import com.example.api.ViewModel.TaskUiState // Import TaskUiState
import com.example.api.ViewModel.TaskViewModel // Import TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApiTheme {
                val taskViewModel: TaskViewModel = viewModel()

                val uiState by taskViewModel.uiState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskScreen(
                        uiState = uiState,
                        onRetryClick = { taskViewModel.fetchTasks() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(true) {
        if (uiState is TaskUiState.Idle) {
            onRetryClick()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is TaskUiState.Loading -> {
                CircularProgressIndicator()
            }
            is TaskUiState.Success -> {
                if (uiState.tasks.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.tasks) { task ->
                            TaskItem(task = task)
                        }
                    }
                } else {
                    Text(text = "No tasks found.")
                }
            }
            is TaskUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Error: ${uiState.message}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRetryClick) {
                        Text("Retry")
                    }
                }
            }
            is TaskUiState.Idle -> {
                Text(text = "Ready to load tasks.")
            }
        }
    }
}


@Composable
fun TaskItem(task: postItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "User ID: ${task.userId}")
        Text(text = "ID: ${task.id}")
        Text(text = "Title: ${task.title}")
        Text(text = "Body: ${task.body}")
    }
}

