package com.example.latihanpraktikum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.latihanpraktikum.data.Status
import com.example.latihanpraktikum.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val taskState by viewModel.getTaskById(taskId).collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(taskId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Tugas")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            val task = taskState
            if (task == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = task.mataKuliah,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = task.judul,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Deadline: ${task.deadline}",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                PriorityChip(prioritas = task.prioritas)
                                StatusChip(status = task.status)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = "Deskripsi Tugas:",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = task.deskripsi.ifBlank { "Tidak ada deskripsi." },
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Ubah Status Pengerjaan:",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.updateTaskStatus(task, Status.BELUM.value) },
                            modifier = Modifier.weight(1f),
                            enabled = task.status != Status.BELUM.value,
                        ) {
                            Text("Belum")
                        }

                        OutlinedButton(
                            onClick = { viewModel.updateTaskStatus(task, Status.PROSES.value) },
                            modifier = Modifier.weight(1f),
                            enabled = task.status != Status.PROSES.value,
                        ) {
                            Text("Proses")
                        }

                        Button(
                            onClick = { viewModel.updateTaskStatus(task, Status.SELESAI.value) },
                            modifier = Modifier.weight(1f),
                            enabled = task.status != Status.SELESAI.value,
                        ) {
                            Text("Selesai")
                        }
                    }
                }
            }
        }
    }
}
