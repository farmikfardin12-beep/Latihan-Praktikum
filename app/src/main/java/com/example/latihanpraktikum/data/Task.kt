package com.example.latihanpraktikum.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity yang merepresentasikan tabel 'tasks' pada Room Database
 * untuk Aplikasi Manajemen Tugas Mahasiswa.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mataKuliah: String,
    val judul: String,
    val deskripsi: String,
    val deadline: String, // Format tanggal String (misal: "2025-05-20") atau Long (timestamp epoch millis)
    val prioritas: String = Prioritas.SEDANG.value, // "Rendah", "Sedang", atau "Tinggi"
    val status: String = Status.BELUM.value, // "Belum", "Proses", atau "Selesai"
)

/**
 * Enum helper untuk opsi Prioritas Tugas
 */
enum class Prioritas(val value: String) {
    RENDAH("Rendah"),
    SEDANG("Sedang"),
    TINGGI("Tinggi"),
}

/**
 * Enum helper untuk opsi Status Tugas
 */
enum class Status(val value: String) {
    BELUM("Belum"),
    PROSES("Proses"),
    SELESAI("Selesai"),
}
