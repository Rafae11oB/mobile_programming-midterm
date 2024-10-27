package com.example.taskmanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagementapp.ui.screens.ModifyTaskScreen
import com.example.taskmanagementapp.ui.screens.TaskListScreen
import com.example.taskmanagementapp.ui.viewmodels.TaskViewModel


class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagementApp(taskViewModel)
        }
    }
}

@Composable
fun TaskManagementApp(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TaskListScreen(taskViewModel = taskViewModel, navController = navController)
        }
        composable("edit/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")!!.toInt()
            ModifyTaskScreen(taskViewModel = taskViewModel, navController = navController, taskId = taskId)
        }
        composable("create") {
            ModifyTaskScreen(taskViewModel = taskViewModel, navController = navController, taskId = null)
        }
    }
}
