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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.shared.model.ActionMenuItem
import com.nguyenminhkhang.shared.model.SortMenuItem
import com.nguyenminhkhang.taskmanagement.ui.common.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.home.action.ActionDialog
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import com.nguyenminhkhang.taskmanagement.ui.home.sort.SortModalBottomSheet
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.common.topbar.TopBar
import com.nguyenminhkhang.taskmanagement.ui.home.event.MenuEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    sortMenuItems: List<SortMenuItem>,
    actionMenuItems: List<ActionMenuItem>,
    uiState: HomeUiState,
    onNavigateToTaskDetail: (Long) -> Unit,
    snackBarHostState: SnackbarHostState,
    onEvent: (HomeEvent) -> Unit,
    onScreenShown: () -> Unit
) {
    LaunchedEffect(Unit) {
        onScreenShown()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
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

        SnackbarHost(hostState = snackBarHostState)

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

        if (uiState.isSortDialogVisible) {
            SortModalBottomSheet(
                items = sortMenuItems,
                onDismiss = {
                    onEvent(MenuEvent.DismissSortDialog)
                }
            )
        }

        if (uiState.isActionBottomSheetVisible) {
            ActionDialog(
                items = actionMenuItems,
                onDismiss = {
                    onEvent(MenuEvent.DismissActionBottomSheet)
                }
            )
        }
    }
}