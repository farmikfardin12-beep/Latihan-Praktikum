package com.example.latihanpraktikum.repository

import com.example.latihanpraktikum.data.Task
import com.example.latihanpraktikum.data.TaskDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository yang mengabstraksi sumber data (TaskDao) dari lapisan UI / ViewModel.
 */
class TaskRepository(private val taskDao: TaskDao) {

    /**
     * Flow berisi daftar seluruh tugas yang diurutkan berdasarkan deadline.
     */
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * Mengambil detail satu tugas berdasarkan ID.
     */
    fun getTaskById(id: Int): Flow<Task?> {
        return taskDao.getTaskById(id)
    }

    /**
     * Menambahkan tugas baru.
     */
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    /**
     * Memperbarui data tugas.
     */
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    /**
     * Menambahkan tugas baru atau memperbarui jika sudah ada (Upsert).
     */
    suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    /**
     * Menghapus tugas dari database.
     */
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    /**
     * Mengubah status pengerjaan tugas (misal: "Belum", "Proses", "Selesai").
     */
    suspend fun updateTaskStatus(task: Task, newStatus: String) {
        val updatedTask = task.copy(status = newStatus)
        taskDao.updateTask(updatedTask)
    }
}
