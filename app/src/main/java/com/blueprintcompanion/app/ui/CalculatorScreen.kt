package com.blueprintcompanion.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blueprintcompanion.app.domain.LengthUnit
import com.blueprintcompanion.app.domain.areaUnitLabel
import com.blueprintcompanion.app.domain.calculate
import com.blueprintcompanion.app.domain.formatNumber
import com.blueprintcompanion.app.domain.fromFeet
import com.blueprintcompanion.app.domain.linearUnitLabel
import com.blueprintcompanion.app.domain.toFeet
import com.blueprintcompanion.app.domain.volumeUnitLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onBack: () -> Unit) {
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(LengthUnit.FEET) }

    val lengthFt = length.toDoubleOrNull()?.let { toFeet(it, unit) }
    val widthFt = width.toDoubleOrNull()?.let { toFeet(it, unit) }
    val heightFt = height.toDoubleOrNull()?.let { toFeet(it, unit) }
    val result = calculate(lengthFt, widthFt, heightFt)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Measurement calculator") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Enter the dimensions you have. Area and perimeter need length and width. Volume also needs height.")
            UnitPicker(unit) { unit = it }
            NumberField("Length", length) { length = it }
            NumberField("Width", width) { width = it }
            NumberField("Height", height) { height = it }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Results", style = MaterialTheme.typography.titleMedium)
                    ResultLine(
                        "Area",
                        result.areaSqFt?.let { "${formatNumber(displayArea(it, unit))} ${areaUnitLabel(unit)}" }
                    )
                    ResultLine(
                        "Perimeter",
                        result.perimeterFt?.let { "${formatNumber(fromFeet(it, unit))} ${linearUnitLabel(unit)}" }
                    )
                    ResultLine(
                        "Volume",
                        result.volumeCuFt?.let { "${formatNumber(displayVolume(it, unit))} ${volumeUnitLabel(unit)}" }
                    )
                    Text("Also in feet: ${feetSummary(result)}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
