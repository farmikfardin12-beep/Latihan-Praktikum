# Pemrograman Aplikasi Bergerak

**Nama:** Farhan Mikdam  
**NIM:** 2411078 
**Kelas:** IFB5B  
**Mata Kuliah:** Pemrograman Aplikasi Bergerak  

# 🎓 Aplikasi Manajemen Tugas Mahasiswa

Aplikasi Android modern yang dirancang untuk membantu mahasiswa mengelola, memantau, dan menyelesaikan tugas-tugas perkuliahan secara terorganisir, produktif, dan efisien.

---

## 🚀 Fitur Utama

- **📋 Menampilkan Daftar Tugas**: Menampilkan seluruh tugas perkuliahan yang diurutkan berdasarkan tanggal *deadline* terdekat.
- **➕ Tambah Tugas Baru**: Menambahkan tugas baru lengkap dengan data Mata Kuliah, Judul, Deskripsi, Tanggal Deadline, Prioritas (*Rendah, Sedang, Tinggi*), dan Status (*Belum, Proses, Selesai*).
- **🔍 Detail Tugas**: Melihat rincian lengkap tugas perkuliahan beserta opsi aksi lanjutan.
- **✏️ Edit Tugas**: Memperbarui informasi tugas yang sudah tersimpan.
- **🗑️ Hapus Tugas**: Menghapus data tugas yang tidak lagi diperlukan dari database lokal dengan dialog konfirmasi.
- **🔄 Ubah Status Pengerjaan**: Memperbarui status tugas secara dinamis dari *Belum*, *Proses*, hingga *Selesai*.

---

## 🛠️ Arsitektur Solusi & Teknologi

Aplikasi ini dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan **Repository Pattern** untuk memastikan pemisahan tanggung jawab (*separation of concerns*), kejelasan alur data, serta skalabilitas kode.

```
       ┌────────────────────────┐
       │   Jetpack Compose UI   │
       └───────────▲────────────┘
                   │ StateFlow
       ┌───────────▼────────────┐
       │     TaskViewModel      │
       └───────────▲────────────┘
                   │ Coroutines / Flow
       ┌───────────▼────────────┐
       │     TaskRepository     │
       └───────────▲────────────┘
                   │ TaskDao
       ┌───────────▼────────────┘
       │  Room Local Database   │
       └────────────────────────┘
```

### Stack Teknologi & Library:
- **Bahasa Pemrograman**: Kotlin
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
- **Komponen Arsitektur**:
  - `ViewModel` & `AndroidViewModel` (Manajemen UI State & siklus hidup)
  - `StateFlow` & `Flow` (Manajemen aliran data asinkron secara reaktif)
  - `Navigation Compose` (Pengaturan navigasi antarlayar)
- **Database Lokal**: [Room Database](https://developer.android.com/training/data-storage/room)
- **Annotation Processor**: [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)
- **Manajemen Dependensi**: Gradle Version Catalog (`libs.versions.toml`)

---

## 💻 Panduan Cara Menjalankan Aplikasi

### Prasyarat
- **Android Studio** (Disarankan versi Ladybug / 2024.2.1 atau yang lebih baru).
- **JDK 17** atau yang kompatibel.
- **Android SDK**: API Minimum `24` (Android 7.0 Nougat) dan SDK Target `37`.

### Langkah-langkah
1. **Clone Repository**:
   ```bash
   git clone https://github.com/username/Aplikasi-Manajemen-Tugas-Mahasiswa.git
   cd Aplikasi-Manajemen-Tugas-Mahasiswa
   ```

2. **Buka Proyek di Android Studio**:
   - Jalankan Android Studio, pilih **Open**, lalu arahkan ke folder proyek ini.

3. **Sinkronkan Gradle (Gradle Sync)**:
   - Tunggu hingga Android Studio selesai mengunduh dependensi dan melakukan *Gradle Sync*.

4. **Jalankan Aplikasi**:
   - Pilih peranti emulator Android (misal: Pixel 8 with API 34+) atau peranti fisik Android via USB Debugging.
   - Klik tombol **Run** (atau tekan `Shift + F10`) di Android Studio.

---

## 📝 Refleksi Singkat

### Keputusan Desain
- **Deklaratif dengan Jetpack Compose**: Memilih Jetpack Compose sepenuhnya tanpa XML Layout untuk mempercepat pengembangan UI, keterbacaan kode, serta kemudahan pengelolaan state secara reaktif.
- **Room Database + KSP**: Menggunakan Room dengan KSP untuk menjamin keamanan tipe data (*compile-time safety*), performa pemrosesan anotasi yang tinggi, dan integrasi alami dengan Kotlin Coroutines/Flow.
- **Single Source of Truth**: Repository bertindak sebagai sumber data tunggal yang mengalirkan data ke UI melalui `StateFlow` yang peka terhadap siklus hidup aplikasi (`SharingStarted.WhileSubscribed(5000)`).

### Kendala & Solusi
- **Generasi Kode Room (`AppDatabase_Impl` does not exist)**:
  - *Kendala*: Terjadi *runtime exception* saat inisialisasi database Room karena penggunaan `annotationProcessor` pada proyek berbasis Kotlin, sehingga kelas implementasi `AppDatabase_Impl` tidak ter-generate.
  - *Solusi*: Mengintegrasikan plugin KSP (`com.google.devtools.ksp`) dan mengganti deklarasi anotasi compiler menjadi `ksp(libs.androidx.room.compiler)` di Gradle.

### Rencana Pengembangan Berikutnya
- **⏰ Notifikasi & Pengingat Deadline**: Mengintegrasikan `WorkManager` / `AlarmManager` untuk mengirimkan notifikasi lokal saat deadline tugas mendekat.
- **🔍 Fitur Filter & Pencarian**: Menambahkan bilah pencarian (*search bar*) serta filter berdasar mata kuliah, prioritas, atau status tugas.
- **🎨 Custom Categorization & Dark Theme**: Menambahkan opsi skema warna berdasarkan mata kuliah serta dukungan mode gelap (*Dark Mode*).
- **☁️ Cloud Backup & Sync**: Menghubungkan penyimpanan lokal Room ke *cloud backend* (seperti Firebase Firestore) agar data terhubung di berbagai perangkat.
