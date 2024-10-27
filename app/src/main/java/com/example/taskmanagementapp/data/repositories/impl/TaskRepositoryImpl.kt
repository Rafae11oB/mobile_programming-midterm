package com.example.taskmanagementapp.data.repositories.impl

import com.example.taskmanagementapp.data.repositories.TaskRepository
import com.example.taskmanagementapp.domain.models.Task
import java.time.LocalDate

object TaskRepositoryImpl : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun add(
        title: String,
        description: String,
        dueDate: LocalDate?
    ) {
        val newTask = Task(
            title = title,
            description = description,
            dueDate = dueDate,
        )
        tasks.add(newTask)
    }

    override fun getAll(): List<Task> {
        return tasks.sortedByDescending { it.createdAt }
    }

    override fun edit(
        id: Int,
        title: String,
        description: String,
        dueDate: LocalDate?
    ) {
        for (i in tasks.indices) {
            if (tasks[i].id == id) {
                tasks[i] = tasks[i].copy(
                    title = title,
                    description = description,
                    dueDate = dueDate
                )
            }
        }
    }

    override fun complete(id: Int) {
        for (i in tasks.indices) {
            if (tasks[i].id == id) {
                tasks[i] = tasks[i].copy(
                    isComplete = true
                )
            }
        }
    }

    override fun delete(id: Int) {
        tasks.removeIf { it.id == id }
    }
}