package com.blueprintcompanion.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blueprintcompanion.app.data.SpaceEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailScreen(
    propertyId: Long,
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    val property by viewModel.property(propertyId).collectAsState(initial = null)
    val spaces by viewModel.spaces(propertyId).collectAsState(initial = emptyList())
    var editingProperty by remember { mutableStateOf(false) }
    var editingSpace by remember { mutableStateOf<SpaceEntity?>(null) }
    var creatingSpace by remember { mutableStateOf(false) }
    var deletingProperty by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) viewModel.attachPlan(propertyId, uri)
    }

    val current = property
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(current?.name ?: "Property") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (current != null) {
                        TextButton(onClick = { editingProperty = true }) { Text("Edit") }
                        IconButton(onClick = { deletingProperty = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete property")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { creatingSpace = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add room")
            }
        }
    ) { padding ->
        if (current == null) {
            Text(
                "This property is no longer available.",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        } else {
            PropertyBody(
                padding = padding,
                property = current,
                spaces = spaces,
                onPickPlan = { picker.launch("image/*") },
                onClearPlan = { viewModel.clearPlan(current) },
                onEditSpace = { editingSpace = it },
                onDeleteSpace = { viewModel.deleteSpace(it) }
            )
        }
    }

    if (deletingProperty && current != null) {
        ConfirmDialog(
            title = "Delete ${current.name}?",
            message = "This removes the property, its rooms, and its floor plan reference.",
            onDismiss = { deletingProperty = false },
            onConfirm = {
                deletingProperty = false
                viewModel.deleteProperty(current)
                onBack()
            }
        )
    }

    if (editingProperty && current != null) {
        PropertyDialog(
            title = "Edit property",
            initialName = current.name,
            initialAddress = current.address,
            initialNotes = current.notes,
            onDismiss = { editingProperty = false },
            onSave = { name, address, notes ->
                viewModel.updateProperty(current.copy(name = name, address = address, notes = notes))
                editingProperty = false
            }
        )
    }

    if (creatingSpace) {
        SpaceDialog(
            title = "Add room",
            initial = null,
            propertyId = propertyId,
            onDismiss = { creatingSpace = false },
            onSave = {
                viewModel.saveSpace(it)
                creatingSpace = false
            }
        )
    }

    editingSpace?.let { space ->
        SpaceDialog(
            title = "Edit room",
            initial = space,
            propertyId = propertyId,
            onDismiss = { editingSpace = null },
            onSave = {
                viewModel.saveSpace(it)
                editingSpace = null
            }
        )
    }
}
