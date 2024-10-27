package com.example.taskmanagementapp.domain.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    val id: Int = generateId(),
    var title: String,
    var description: String,
    var dueDate: LocalDate? = null,
    var isComplete: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        private var id = 0

        private fun generateId(): Int {
            return ++id
        }
    }
}
