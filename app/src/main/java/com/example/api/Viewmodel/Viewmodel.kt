package com.example.api.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.Model.Api.ApiClient
import com.example.api.Model.data.post // post là ArrayList<postItem>
import com.example.api.Model.data.postItem // Import postItem thay vì DataX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class TaskUiState {
    object Loading : TaskUiState()
    // Trạng thái thành công chứa danh sách các đối tượng postItem
    data class Success(val tasks: List<postItem>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
    object Idle : TaskUiState() // Trạng thái ban đầu hoặc không có tác vụ
}

class TaskViewModel : ViewModel() {

    // StateFlow để giữ trạng thái UI và thông báo cho UI khi có thay đổi
    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Idle)
    val uiState: StateFlow<TaskUiState> = _uiState

    // Hàm để gọi API và cập nhật trạng thái
    fun fetchTasks() {
        // Chỉ fetch nếu không đang ở trạng thái Loading
        if (_uiState.value == TaskUiState.Loading) return

        _uiState.value = TaskUiState.Loading // Cập nhật trạng thái đang tải

        viewModelScope.launch {
            try {
                // Gọi API sử dụng suspend fun.
                // Retrofit sẽ tự động xử lý phản hồi thành đối tượng 'post' (ArrayList<postItem>).
                val response = ApiClient.api.getPost() // Giả sử getPosts trả về Response<post>

                if (response.isSuccessful) {
                    val postsList = response.body()
                    if (postsList != null) {
                        // Cập nhật trạng thái thành công với danh sách các đối tượng postItem
                        _uiState.value = TaskUiState.Success(postsList)
                    } else {
                        // Xử lý trường hợp body rỗng nhưng response vẫn thành công
                        _uiState.value = TaskUiState.Error("Phản hồi rỗng từ máy chủ.")
                    }
                } else {
                    // Xử lý lỗi từ server (ví dụ: 404, 500)
                    val errorMessage = response.errorBody()?.string() ?: "Lỗi không xác định từ máy chủ: ${response.code()}"
                    _uiState.value = TaskUiState.Error(errorMessage)
                }
            } catch (e: IOException) {
                // Xử lý lỗi mạng (ví dụ: không có kết nối internet)
                _uiState.value = TaskUiState.Error("Lỗi mạng: ${e.message}")
            } catch (e: Exception) {
                // Xử lý các lỗi khác (ví dụ: lỗi parse JSON nếu cấu trúc thực tế khác một chút)
                _uiState.value = TaskUiState.Error("Lỗi không xác định: ${e.message}")
            }
        }
    }
}