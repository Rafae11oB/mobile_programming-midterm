package com.example.taskmanagementapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskmanagementapp.ui.viewmodels.TaskViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
private object FutureOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val minDate = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        return utcTimeMillis >= minDate
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTaskScreen(taskViewModel: TaskViewModel, navController: NavController, taskId: Int?) {
    val existingTask = if (taskId != null) taskViewModel.getTaskById(taskId) else null
    var newTaskTitle by remember(taskId) { mutableStateOf(existingTask?.title ?: "") }
    var newTaskDescription by remember(taskId) { mutableStateOf(existingTask?.description ?: "") }
    var newDueDate by remember(taskId) { mutableStateOf(existingTask?.dueDate) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = newDueDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = FutureOrPresentSelectableDates
    )

    var isDatePickerVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textStyle = MaterialTheme.typography.titleMedium,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.padding(8.dp)) {
                        if (newTaskTitle.isEmpty())
                            Text(
                                "Enter title",
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        innerTextField()
                    }
                }
            )
            BasicTextField(
                value = newTaskDescription,
                onValueChange = { newTaskDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.padding(8.dp)) {
                        if (newTaskDescription.isEmpty()) Text("Add details")
                        innerTextField()
                    }
                }
            )
            TextButton(
                onClick = {
                    isDatePickerVisible = true
                },
            ) {
                Text(
                    text = newDueDate?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) ?: "Select Due Date"
                )
            }
            if (isDatePickerVisible) {
                DatePickerDialog(
                    onDismissRequest = {
                        isDatePickerVisible = false
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            newDueDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0L)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            isDatePickerVisible = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isDatePickerVisible = false
                        }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                    )
                }
            }
            Button(
                onClick = {
                    if (newTaskTitle.isNotEmpty()) {
                        if (taskId == null) {
                            taskViewModel.addTask(newTaskTitle, newTaskDescription, newDueDate)
                        } else {
                            taskViewModel.editTask(taskId, newTaskTitle, newTaskDescription, newDueDate)
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            ) {
                Text("Save")
            }
        }
    }
}