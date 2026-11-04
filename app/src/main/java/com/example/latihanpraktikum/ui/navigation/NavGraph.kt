package com.example.latihanpraktikum.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.latihanpraktikum.ui.screens.AddScreen
import com.example.latihanpraktikum.ui.screens.DetailScreen
import com.example.latihanpraktikum.ui.screens.EditScreen
import com.example.latihanpraktikum.ui.screens.HomeScreen
import com.example.latihanpraktikum.ui.viewmodel.TaskViewModel
import com.example.latihanpraktikum.ui.viewmodel.TaskViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add")
    object Detail : Screen("detail/{taskId}") {
        fun createRoute(taskId: Int) = "detail/$taskId"
    }
    object Edit : Screen("edit/{taskId}") {
        fun createRoute(taskId: Int) = "edit/$taskId"
    }
}

@Composable
fun TaskNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(
            LocalContext.current.applicationContext as Application,
        ),
    ),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        // 1. Layar Daftar Tugas (Home)
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddTaskClick = { navController.navigate(Screen.Add.route) },
                onTaskClick = { taskId -> navController.navigate(Screen.Detail.createRoute(taskId)) },
            )
        }

        // 2. Layar Tambah Tugas (Add)
        composable(Screen.Add.route) {
            AddScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
            )
        }

        // 3. Layar Detail Tugas (Detail)
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            DetailScreen(
                taskId = taskId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.Edit.createRoute(id)) },
            )
        }

        // 4. Layar Edit Tugas (Edit)
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            EditScreen(
                taskId = taskId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onTaskDeleted = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
            )
        }
    }
}
