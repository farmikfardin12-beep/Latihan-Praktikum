package com.example.latihanpraktikum.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.latihanpraktikum.data.AppDatabase
import com.example.latihanpraktikum.data.Task
import com.example.latihanpraktikum.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Representasi UI State untuk daftar tugas.
 */
sealed interface TasksUiState {
    object Loading : TasksUiState
    data class Success(val tasks: List<Task>) : TasksUiState
    data class Error(val message: String) : TasksUiState
}

/**
 * ViewModel untuk mengelola UI State dan aksi pengguna pada Manajemen Tugas Mahasiswa.
 * Menggunakan AndroidViewModel agar aman terhadap perubahan konfigurasi layar (orientation change).
 */
class TaskViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: TaskRepository = TaskRepository(AppDatabase.getDatabase(application).taskDao()),
) : AndroidViewModel(application) {

    /**
     * StateFlow yang memaparkan daftar tugas terkini ke Compose UI.
     * Menggunakan SharingStarted.WhileSubscribed(5000) untuk efisiensi resource saat app di background.
     */
    val tasksUiState: StateFlow<TasksUiState> = repository.allTasks
        .map { tasks -> TasksUiState.Success(tasks) as TasksUiState }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TasksUiState.Loading,
        )

    /**
     * Mengambil detail tugas berdasarkan ID (mengembalikan Flow<Task?>).
     */
    fun getTaskById(id: Int) = repository.getTaskById(id)

    /**
     * Menambah tugas baru.
     */
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    /**
     * Memperbarui data tugas (Edit).
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    /**
     * Menyimpan tugas (Insert atau Update).
     */
    fun upsertTask(task: Task) {
        viewModelScope.launch {
            repository.upsertTask(task)
        }
    }

    /**
     * Menghapus tugas dari database.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    /**
     * Mengubah status pengerjaan tugas (misal: "Belum", "Proses", "Selesai").
     */
    fun updateTaskStatus(task: Task, newStatus: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(task, newStatus)
        }
    }
}

/**
 * Factory class untuk menjamin pembentukan TaskViewModel secara aman di Jetpack Compose.
 */
class TaskViewModelFactory(
    private val application: Application,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
