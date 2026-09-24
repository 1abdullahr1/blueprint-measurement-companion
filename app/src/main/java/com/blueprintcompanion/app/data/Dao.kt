package com.blueprintcompanion.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    fun observe(id: Long): Flow<PropertyEntity?>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getById(id: Long): PropertyEntity?

    @Insert
    suspend fun insert(property: PropertyEntity): Long

    @Update
    suspend fun update(property: PropertyEntity)

    @Query("DELETE FROM properties WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE properties SET planImagePath = :path WHERE id = :id")
    suspend fun updatePlan(id: Long, path: String?)
}

@Dao
interface SpaceDao {
    @Query("SELECT * FROM spaces WHERE propertyId = :propertyId ORDER BY name COLLATE NOCASE")
    fun observeForProperty(propertyId: Long): Flow<List<SpaceEntity>>

    @Insert
    suspend fun insert(space: SpaceEntity): Long

    @Update
    suspend fun update(space: SpaceEntity)

    @Query("DELETE FROM spaces WHERE id = :id")
    suspend fun delete(id: Long)
}
