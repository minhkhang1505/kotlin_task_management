package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.NavScreen
import com.nguyenminhkhang.taskmanagement.ui.common.navigationbar.NavigationBottomBar
import com.nguyenminhkhang.taskmanagement.ui.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import com.nguyenminhkhang.taskmanagement.ui.home.state.SearchState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(
    searchResults: List<TaskEntity>,
    searchState: SearchState,
    uiState: HomeUiState,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onEvent: (HomeEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(onEvent)
        },
        modifier = Modifier.fillMaxSize().background(Color.Yellow),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        floatingActionButton = {
            AppFloatActionButton{ onEvent(HomeEvent.ShowAddTaskSheet) }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PagerTabLayout(uiState, onEvent, navController)
        }

        if (searchState.isSearchBarVisible) {
            CustomSearchBar(
                searchResult = searchResults,
                searchState = searchState,
                onEvent = onEvent
            )
        }

        if (uiState.isNewCollectionNameDialogVisible) {
            RenameCollectionDialog(
                newCollectionName = uiState.newCollectionName,
                onEvent = onEvent
            )
        }

        if (uiState.isAddTaskSheetVisible) {
            AddTaskBottomSheet(uiState = uiState, onEvent = onEvent,)
        }

        if (uiState.menuListButtonSheet.isNullOrEmpty() == false) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(HomeEvent.ResetMenuListButtonSheet) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.menuListButtonSheet.forEach { item ->
                        Text(item.title, modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                item.action.invoke()
                                onEvent(HomeEvent.ResetMenuListButtonSheet)
                            }
                        )
                    }
                }
            }
        }
    }
}