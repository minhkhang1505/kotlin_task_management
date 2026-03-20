package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.domain.model.ActionMenuItem
import com.nguyenminhkhang.taskmanagement.domain.model.SortMenuItem
import com.nguyenminhkhang.taskmanagement.ui.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.home.action.ActionDialog
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import com.nguyenminhkhang.taskmanagement.ui.home.sort.SortDialog
import com.nguyenminhkhang.taskmanagement.ui.home.sort.buildSortMenuItems
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(
    sortMenuItems: List<SortMenuItem>,
    actionMenuItems: List<ActionMenuItem>,
    uiState: HomeUiState,
    onNavigateToTaskDetail: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    onEvent: (HomeEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            PagerTabLayout(uiState, onEvent, onNavigateToTaskDetail)
        }

        SnackbarHost(hostState = snackbarHostState)

        AppFloatActionButton(
            onClick = { onEvent(UiEvent.ShowAddTaskSheet) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )

        if (uiState.isNewCollectionNameDialogVisible) {
            RenameCollectionDialog(
                newCollectionName = uiState.newCollectionName,
                onEvent = onEvent
            )
        }

        if (uiState.isAddTaskSheetVisible) {
            AddTaskBottomSheet(uiState = uiState, onEvent = onEvent,)
        }

        if (uiState.sortMenuButtonSheet.isNullOrEmpty() == false) {
            SortDialog(
                items = sortMenuItems,
                onDismiss = {  }
            )
        }

        if (uiState.actionMenuButtonSheet.isNullOrEmpty() == false) {
            ActionDialog(
                items = actionMenuItems,
                onDismiss = {  }
            )
        }
    }
}