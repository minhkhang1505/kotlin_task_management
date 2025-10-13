package com.nguyenminhkhang.taskmanagement.ui.home.event

sealed class CollectionEvent : HomeEvent {
    data class CurrentCollectionId(val collectionId: Long) : CollectionEvent()
    data class AddNewCollectionRequested(val name: String) : CollectionEvent()
    data class OnCollectionNameChanged(val name: String) : CollectionEvent()
    object NewCollectionNameCleared : CollectionEvent()

    // Rename collection
    object HideRenameCollectionDialog : CollectionEvent()
    object ShowRenameCollectionDialog : CollectionEvent()
    object ClearRenameCollectionName : CollectionEvent()
    data class RenameCollection(val newCollectionName: String) : CollectionEvent()

    // Sort
    data class UpdateCollectionRequested(val collectionId: Long) : CollectionEvent()

    // Delete collection
    data class DeleteCollection(val collectionId: Long) : CollectionEvent()
}