package com.blueprintcompanion.app.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.blueprintcompanion.app.data.PropertyEntity
import com.blueprintcompanion.app.data.Repository
import com.blueprintcompanion.app.data.SpaceEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    val properties = repository.observeProperties()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun property(id: Long) = repository.observeProperty(id)
    fun spaces(propertyId: Long) = repository.observeSpaces(propertyId)

    fun createProperty(name: String, address: String, notes: String, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.createProperty(name, address, notes)
            onCreated(id)
        }
    }

    fun updateProperty(property: PropertyEntity) {
        viewModelScope.launch { repository.updateProperty(property) }
    }

    fun deleteProperty(property: PropertyEntity) {
        viewModelScope.launch { repository.deletePropertyAndPlan(property) }
    }

    fun attachPlan(propertyId: Long, uri: Uri) {
        viewModelScope.launch { repository.savePlan(propertyId, uri) }
    }

    fun clearPlan(property: PropertyEntity) {
        viewModelScope.launch { repository.clearPlan(property) }
    }

    fun saveSpace(space: SpaceEntity) {
        viewModelScope.launch { repository.saveSpace(space) }
    }

    fun deleteSpace(id: Long) {
        viewModelScope.launch { repository.deleteSpace(id) }
    }
}
