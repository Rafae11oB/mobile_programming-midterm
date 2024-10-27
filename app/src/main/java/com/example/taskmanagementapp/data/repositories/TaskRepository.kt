package com.example.taskmanagementapp.data.repositories

import com.example.taskmanagementapp.domain.models.Task
import java.time.LocalDate

interface TaskRepository {
    fun add(
        title: String,
        description: String,
        dueDate: LocalDate?,
    )

    fun getAll(): List<Task>

    fun edit(
        id: Int,
        title: String,
        description: String,
        dueDate: LocalDate?,
    )

    fun complete(id: Int)

    fun delete(id: Int)
}