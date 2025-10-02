package com.nguyenminhkhang.taskmanagement.domain.usecase

import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleCompleteUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    /**
     * Dùng 'operator fun invoke' cho phép chúng ta gọi instance của class này
     * như một hàm. Ví dụ: toggleCompleteUseCase(taskId, true)
     *
     * @param taskId ID của task cần cập nhật.
     * @param isCompleted Trạng thái MỚI (true hoặc false) của task.
     * @return Trả về true nếu cập nhật thành công.
     */
    suspend operator fun invoke(taskId: Long, isCompleted: Boolean): Boolean {
        // Chỉ đơn giản là ủy quyền công việc cho repository.
        // Nếu sau này có thêm logic (ví dụ: ghi log, kiểm tra quyền)
        // thì sẽ thêm vào đây mà không cần sửa ViewModel.
        return taskRepository.updateTaskCompleted(taskId, isCompleted)
    }
}