package com.blueprintcompanion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    viewModel: AppViewModel,
    onOpen: (Long) -> Unit,
    onCalculator: () -> Unit
) {
    val properties by viewModel.properties.collectAsState()
    var showCreate by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Blueprint Companion") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreate = true }) {
                Icon(Icons.Default.Add, contentDescription = "New property")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "A measurement notebook for properties, rooms, and floor plans.",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedButton(onClick = onCalculator, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Text("  Open calculator")
            }
            if (properties.isEmpty()) {
                Text("No properties yet. Add a house, lot, or job to start recording rooms.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(properties, key = { it.id }) { property ->
                        Card(onClick = { onOpen(property.id) }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(property.name, style = MaterialTheme.typography.titleMedium)
                                if (property.address.isNotBlank()) {
                                    Text(property.address, style = MaterialTheme.typography.bodySmall)
                                }
                                Text(
                                    if (property.planImagePath != null) "Floor plan attached" else "No floor plan yet",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreate) {
        PropertyDialog(
            title = "New property",
            initialName = "",
            initialAddress = "",
            initialNotes = "",
            onDismiss = { showCreate = false },
            onSave = { name, address, notes ->
                viewModel.createProperty(name, address, notes) { id ->
                    showCreate = false
                    onOpen(id)
                }
            }
        )
    }
}
