package com.blueprintcompanion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blueprintcompanion.app.data.SpaceEntity
import com.blueprintcompanion.app.domain.LengthUnit
import com.blueprintcompanion.app.domain.areaUnitLabel
import com.blueprintcompanion.app.domain.calculate
import com.blueprintcompanion.app.domain.formatNumber
import com.blueprintcompanion.app.domain.toFeet

@Composable
fun PropertyDialog(
    title: String,
    initialName: String,
    initialAddress: String,
    initialNotes: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var address by remember { mutableStateOf(initialAddress) }
    var notes by remember { mutableStateOf(initialNotes) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Name") }, singleLine = true)
                OutlinedTextField(address, { address = it }, label = { Text("Address") }, singleLine = true)
                OutlinedTextField(notes, { notes = it }, label = { Text("Notes") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name.trim(), address.trim(), notes.trim()) },
                enabled = name.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun SpaceDialog(
    title: String,
    initial: SpaceEntity?,
    propertyId: Long,
    onDismiss: () -> Unit,
    onSave: (SpaceEntity) -> Unit
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var length by remember { mutableStateOf(initial?.length?.let { formatNumber(it) } ?: "") }
    var width by remember { mutableStateOf(initial?.width?.let { formatNumber(it) } ?: "") }
    var height by remember { mutableStateOf(initial?.height?.let { formatNumber(it) } ?: "") }
    var notes by remember { mutableStateOf(initial?.notes ?: "") }
    var unit by remember { mutableStateOf(LengthUnit.fromSymbol(initial?.unitSymbol ?: LengthUnit.FEET.symbol)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(name, { name = it }, label = { Text("Room or space") }, singleLine = true)
                UnitPicker(unit) { unit = it }
                NumberField("Length", length) { length = it }
                NumberField("Width", width) { width = it }
                NumberField("Height", height) { height = it }
                OutlinedTextField(notes, { notes = it }, label = { Text("Notes") })
                val preview = calculate(
                    length.toDoubleOrNull()?.let { toFeet(it, unit) },
                    width.toDoubleOrNull()?.let { toFeet(it, unit) },
                    height.toDoubleOrNull()?.let { toFeet(it, unit) }
                )
                val area = preview.areaSqFt?.let {
                    formatNumber(displayArea(it, unit)) + " " + areaUnitLabel(unit)
                } ?: "—"
                Text("Area $area")
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        SpaceEntity(
                            id = initial?.id ?: 0,
                            propertyId = propertyId,
                            name = name.trim(),
                            length = length.toDoubleOrNull(),
                            width = width.toDoubleOrNull(),
                            height = height.toDoubleOrNull(),
                            unitSymbol = unit.symbol,
                            notes = notes.trim()
                        )
                    )
                }
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
