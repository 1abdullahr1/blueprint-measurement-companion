package com.blueprintcompanion.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val notes: String,
    val planImagePath: String?,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "spaces",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("propertyId")]
)
data class SpaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val propertyId: Long,
    val name: String,
    val length: Double?,
    val width: Double?,
    val height: Double?,
    val unitSymbol: String,
    val notes: String
)
