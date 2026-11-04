package com.example.latihanpraktikum.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.latihanpraktikum.data.Prioritas
import com.example.latihanpraktikum.data.Status
import com.example.latihanpraktikum.data.Task
import com.example.latihanpraktikum.ui.viewmodel.TaskViewModel
import com.example.latihanpraktikum.ui.viewmodel.TasksUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.tasksUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Tugas Mahasiswa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Tugas")
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                is TasksUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is TasksUiState.Error -> {
                    Text(
                        text = "Terjadi kesalahan: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is TasksUiState.Success -> {
                    if (state.tasks.isEmpty()) {
                        Text(
                            text = "Belum ada tugas. Klik tombol '+' untuk menambah.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(state.tasks, key = { it.id }) { task ->
                                TaskItemCard(
                                    task = task,
                                    onClick = { onTaskClick(task.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItemCard(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = task.mataKuliah,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Deadline: ${task.deadline}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = task.judul,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Priority Badge
                PriorityChip(prioritas = task.prioritas)

                // Status Badge
                StatusChip(status = task.status)
            }
        }
    }
}

@Composable
fun PriorityChip(prioritas: String) {
    val (bgColor, textColor) = when (prioritas) {
        Prioritas.TINGGI.value -> Color(0xFFFFCDD2) to Color(0xFFB71C1C)
        Prioritas.SEDANG.value -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
        else -> Color(0xFFC8E6C9) to Color(0xFF1B5E20)
    }

    SuggestionChip(
        onClick = { },
        label = { Text("Prioritas: $prioritas", style = MaterialTheme.typography.labelSmall) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = bgColor,
            labelColor = textColor,
        ),
    )
}

@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor) = when (status) {
        Status.SELESAI.value -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        Status.PROSES.value -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        else -> Color(0xFFECEFF1) to Color(0xFF455A64)
    }

    SuggestionChip(
        onClick = { },
        label = { Text(status, style = MaterialTheme.typography.labelSmall) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = bgColor,
            labelColor = textColor,
        ),
    )
}
