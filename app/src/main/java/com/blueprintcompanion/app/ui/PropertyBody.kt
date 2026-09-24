package com.blueprintcompanion.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image as ImageIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.blueprintcompanion.app.data.PropertyEntity
import com.blueprintcompanion.app.data.SpaceEntity
import com.blueprintcompanion.app.domain.LengthUnit
import com.blueprintcompanion.app.domain.areaUnitLabel
import com.blueprintcompanion.app.domain.calculate
import com.blueprintcompanion.app.domain.dimensionLine
import com.blueprintcompanion.app.domain.formatNumber
import com.blueprintcompanion.app.domain.toFeet
import java.io.File

@Composable
fun PropertyBody(
    padding: PaddingValues,
    property: PropertyEntity,
    spaces: List<SpaceEntity>,
    onPickPlan: () -> Unit,
    onClearPlan: () -> Unit,
    onEditSpace: (SpaceEntity) -> Unit,
    onDeleteSpace: (Long) -> Unit
) {
    val total = spaces.mapNotNull { spaceAreaSqFt(it) }.sum()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            if (property.address.isNotBlank()) {
                Text(property.address, style = MaterialTheme.typography.bodyLarge)
            }
            if (property.notes.isNotBlank()) {
                Text(property.notes, style = MaterialTheme.typography.bodyMedium)
            }
        }
        item {
            PlanCard(property, onPickPlan, onClearPlan)
        }
        item {
            Text("Rooms and spaces", style = MaterialTheme.typography.titleMedium)
            if (spaces.isNotEmpty()) {
                Text("Recorded floor area: ${formatNumber(total)} sq ft")
            }
        }
        if (spaces.isEmpty()) {
            item { Text("Add a living room, bedroom, kitchen, or any other space.") }
        }
        items(spaces, key = { it.id }) { space ->
            SpaceCard(space, onClick = { onEditSpace(space) }, onDelete = { onDeleteSpace(space.id) })
        }
    }
}

@Composable
private fun PlanCard(property: PropertyEntity, onPick: () -> Unit, onClear: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Floor plan", style = MaterialTheme.typography.titleMedium)
            Text("Keep a blueprint or sketch with the measurements for this property.")
            val path = property.planImagePath
            if (path != null && File(path).exists()) {
                Image(
                    painter = rememberAsyncImagePainter(File(path)),
                    contentDescription = "Floor plan",
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    contentScale = ContentScale.Fit
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onPick) { Text("Replace") }
                    TextButton(onClick = onClear) { Text("Remove") }
                }
            } else {
                Button(onClick = onPick) {
                    Icon(ImageIcon, contentDescription = null)
                    Text("  Attach plan")
                }
            }
        }
    }
}

@Composable
private fun SpaceCard(space: SpaceEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    val unit = LengthUnit.fromSymbol(space.unitSymbol)
    val area = spaceAreaSqFt(space)
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(space.name, fontWeight = FontWeight.SemiBold)
                Text(dimensionLine(space.length, space.width, space.height, unit))
                if (area != null) {
                    Text("${formatNumber(displayArea(area, unit))} ${areaUnitLabel(unit)}")
                }
                if (space.notes.isNotBlank()) {
                    Text(space.notes, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete room")
            }
        }
    }
}

private fun spaceAreaSqFt(space: SpaceEntity): Double? {
    val unit = LengthUnit.fromSymbol(space.unitSymbol)
    return calculate(
        space.length?.let { toFeet(it, unit) },
        space.width?.let { toFeet(it, unit) },
        space.height?.let { toFeet(it, unit) }
    ).areaSqFt
}
