package com.nguyenminhkhang.taskmanagement.ui.home.action

import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.domain.model.ActionMenuItem
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import com.nguyenminhkhang.taskmanagement.ui.home.event.CollectionEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import timber.log.Timber

fun buildActionMenuItem(
    strings: StringProvider,
    collectionId: Long,
    onEvent: (HomeEvent) -> Unit
): List<ActionMenuItem> {
    return listOf(
        ActionMenuItem(
            title = strings.getString(R.string.delete_task),
        ) {
            Timber.d("buildActionMenuItem() - Delete Collection action item clicked at collectionId: ${onEvent}")
            onEvent(UiEvent.OnToggleDeleteButton)
        },
        ActionMenuItem(
            title = strings.getString(R.string.delete_collection),
        ) {
            Timber.d("buildActionMenuItem() - Delete Collection action item clicked at collectionId: ${collectionId}")
            onEvent(CollectionEvent.DeleteCollection(collectionId))
        },
        ActionMenuItem(
            title = strings.getString(R.string.rename_collection),
        ) {
            onEvent(CollectionEvent.ClearRenameCollectionName)
            onEvent(CollectionEvent.ShowRenameCollectionDialog)
        }
    )
}