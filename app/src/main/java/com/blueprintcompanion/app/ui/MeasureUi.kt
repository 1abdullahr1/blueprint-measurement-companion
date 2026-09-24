package com.blueprintcompanion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blueprintcompanion.app.domain.LengthUnit
import com.blueprintcompanion.app.domain.MeasurementResult
import com.blueprintcompanion.app.domain.formatNumber

@Composable
fun UnitPicker(selected: LengthUnit, onSelect: (LengthUnit) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        LengthUnit.entries.forEach { unit ->
            if (unit == selected) {
                Button(onClick = { onSelect(unit) }) { Text(unit.symbol) }
            } else {
                OutlinedButton(onClick = { onSelect(unit) }) { Text(unit.symbol) }
            }
        }
    }
}

@Composable
fun NumberField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { raw ->
            if (raw.isEmpty() || raw.matches(Regex("""\d*\.?\d*"""))) onChange(raw)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun ResultLine(label: String, value: String?) {
    Text("$label: ${value ?: "—"}")
}

fun displayArea(areaSqFt: Double, unit: LengthUnit): Double {
    val factor = unit.toFeet
    return areaSqFt / (factor * factor)
}

fun displayVolume(volumeCuFt: Double, unit: LengthUnit): Double {
    val factor = unit.toFeet
    return volumeCuFt / (factor * factor * factor)
}

fun feetSummary(result: MeasurementResult): String {
    val area = result.areaSqFt?.let { "${formatNumber(it)} sq ft" } ?: "area —"
    val perimeter = result.perimeterFt?.let { "${formatNumber(it)} ft" } ?: "perimeter —"
    val volume = result.volumeCuFt?.let { "${formatNumber(it)} cu ft" } ?: "volume —"
    return "$area, $perimeter, $volume"
}
