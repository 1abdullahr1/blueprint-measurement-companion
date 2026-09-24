package com.blueprintcompanion.app.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.UUID

class Repository(private val context: Context) {
    private val db = AppDatabase.get(context)
    private val properties = db.propertyDao()
    private val spaces = db.spaceDao()

    fun observeProperties(): Flow<List<PropertyEntity>> = properties.observeAll()
    fun observeProperty(id: Long): Flow<PropertyEntity?> = properties.observe(id)
    fun observeSpaces(propertyId: Long): Flow<List<SpaceEntity>> = spaces.observeForProperty(propertyId)

    suspend fun createProperty(name: String, address: String, notes: String): Long {
        return properties.insert(
            PropertyEntity(
                name = name.trim(),
                address = address.trim(),
                notes = notes.trim(),
                planImagePath = null
            )
        )
    }

    suspend fun updateProperty(property: PropertyEntity) {
        properties.update(property)
    }

    suspend fun deletePropertyAndPlan(property: PropertyEntity) {
        property.planImagePath?.let { path ->
            File(path).delete()
        }
        properties.delete(property.id)
    }

    suspend fun savePlan(propertyId: Long, source: Uri): String {
        val dir = File(context.filesDir, "plans").apply { mkdirs() }
        val dest = File(dir, "plan-$propertyId-${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(source).use { input ->
            requireNotNull(input) { "Could not read the selected image." }
            dest.outputStream().use { output -> input.copyTo(output) }
        }
        properties.updatePlan(propertyId, dest.absolutePath)
        return dest.absolutePath
    }

    suspend fun clearPlan(property: PropertyEntity) {
        property.planImagePath?.let { File(it).delete() }
        properties.updatePlan(property.id, null)
    }

    suspend fun saveSpace(space: SpaceEntity) {
        if (space.id == 0L) spaces.insert(space) else spaces.update(space)
    }

    suspend fun deleteSpace(id: Long) {
        spaces.delete(id)
    }
}
