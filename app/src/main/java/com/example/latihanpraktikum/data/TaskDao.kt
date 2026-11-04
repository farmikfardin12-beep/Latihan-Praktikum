package com.example.latihanpraktikum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) untuk mengakses tabel 'tasks'
 */
@Dao
interface TaskDao {

    /**
     * Menyimpan data tugas baru atau memperbarui jika sudah ada (Upsert)
     */
    @Upsert
    suspend fun upsertTask(task: Task)

    /**
     * Menambahkan tugas baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /**
     * Memperbarui data tugas
     */
    @Update
    suspend fun updateTask(task: Task)

    /**
     * Menghapus tugas dari database
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * Mengambil semua tugas diurutkan berdasarkan tanggal deadline (ASC)
     */
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Mengambil satu tugas berdasarkan ID
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<Task?>
}
