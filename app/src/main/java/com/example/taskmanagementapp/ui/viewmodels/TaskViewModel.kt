package com.example.taskmanagementapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.taskmanagementapp.data.repositories.TaskRepository
import com.example.taskmanagementapp.data.repositories.impl.TaskRepositoryImpl
import com.example.taskmanagementapp.domain.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class TaskViewModel : ViewModel() {
    private val taskRepository: TaskRepository = TaskRepositoryImpl

    private val _tasks = MutableStateFlow(taskRepository.getAll())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(title: String, description: String, dueDate: LocalDate?) {
        taskRepository.add(
            title = title,
            description = description,
            dueDate = dueDate
        )

        _tasks.update { taskRepository.getAll() }
    }

    fun getTaskById(id: Int): Task? {
        return _tasks.value.find { it.id == id }
    }

    fun deleteTask(id: Int) {
        taskRepository.delete(id)
        _tasks.update { taskRepository.getAll() }
    }

    fun editTask(id: Int, newTitle: String, newDescription: String, dueDate: LocalDate?) {
        taskRepository.edit(
            id = id,
            title = newTitle,
            description = newDescription,
            dueDate = dueDate,
        )

        _tasks.update { taskRepository.getAll() }
    }

    fun toggleTaskCompletion(id: Int) {
        taskRepository.complete(id)
        _tasks.update { taskRepository.getAll() }
    }
}
