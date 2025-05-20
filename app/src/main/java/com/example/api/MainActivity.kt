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
        enableEdgeToEdge() // Bật chế độ hiển thị toàn màn hình
        setContent {
            ApiTheme {
                val taskViewModel: TaskViewModel = viewModel()

                val uiState by taskViewModel.uiState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskScreen(
                        uiState = uiState,
                        onRetryClick = { taskViewModel.fetchTasks() }, // Gọi hàm fetchTasks trong ViewModel khi nhấn Retry
                        modifier = Modifier.padding(innerPadding) // Áp dụng padding từ Scaffold
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

    // Box để căn giữa nội dung trên màn hình.
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Căn giữa nội dung
    ) {
        // Sử dụng when để hiển thị UI khác nhau tùy thuộc vào trạng thái uiState.
        when (uiState) {
            is TaskUiState.Loading -> {
                // Hiển thị indicator tải khi đang ở trạng thái Loading.
                CircularProgressIndicator()
            }
            is TaskUiState.Success -> {
                // Hiển thị danh sách tasks khi ở trạng thái Success.
                // uiState.tasks lúc này là List<postItem> từ ViewModel.
                if (uiState.tasks.isNotEmpty()) {
                    // LazyColumn hiệu quả cho việc hiển thị danh sách lớn.
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Padding cho toàn bộ danh sách
                        verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các item
                    ) {
                        // items là hàm của LazyColumn để hiển thị các phần tử từ một danh sách.
                        items(uiState.tasks) { task ->
                            // Hiển thị từng task item bằng Composable TaskItem.
                            TaskItem(task = task)
                        }
                    }
                } else {
                    // Xử lý trường hợp danh sách rỗng.
                    Text(text = "No tasks found.")
                }
            }
            is TaskUiState.Error -> {
                // Hiển thị thông báo lỗi và nút Retry khi ở trạng thái Error.
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp) // Padding cho cột lỗi
                ) {
                    Text(text = "Error: ${uiState.message}") // Hiển thị thông báo lỗi từ ViewModel
                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách
                    Button(onClick = onRetryClick) { // Nút Retry
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

/**
 * Composable để hiển thị thông tin chi tiết của một task đơn lẻ.
 * @param task Đối tượng postItem chứa thông tin của task.
 * @param modifier Modifier cho Composable này.
 */
@Composable
fun TaskItem(task: postItem, modifier: Modifier = Modifier) { // Sử dụng postItem làm kiểu dữ liệu cho task item
    // Column để sắp xếp các Text theo chiều dọc.
    Column(
        modifier = modifier
            .fillMaxWidth() // Chiếm toàn bộ chiều rộng
            .padding(8.dp) // Padding bên trong mỗi item
    ) {
        Text(text = "ID: ${task.id}")
        Text(text = "Title: ${task.title}")
        Text(text = "Body: ${task.body}") // Đổi từ description sang body
        Text(text = "User ID: ${task.userId}") // Thêm hiển thị userId nếu cần
    }
}

/**
 * Preview cho Composable TaskScreen.
 * Giúp xem trước giao diện ở các trạng thái khác nhau trong Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ApiTheme {
        // Bạn có thể thay đổi trạng thái ở đây để xem trước các UI khác nhau
        // TaskScreen(uiState = TaskUiState.Idle, onRetryClick = {})
        // TaskScreen(uiState = TaskUiState.Loading, onRetryClick = {})
        // TaskScreen(uiState = TaskUiState.Error("Failed to fetch!"), onRetryClick = {})
        // Preview trạng thái Success với dữ liệu mẫu
        TaskScreen(uiState = TaskUiState.Success(
            listOf(
                // Tạo các đối tượng postItem mẫu cho Preview
                postItem(body = "This is the body of the first post.", id = 1, title = "First Post Title", userId = 101),
                postItem(body = "This is the body of the second post.", id = 2, title = "Second Post Title", userId = 102)
            )
        ), onRetryClick = {})
    }
}
