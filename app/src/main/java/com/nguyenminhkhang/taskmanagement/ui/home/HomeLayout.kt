package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.home.state.NewTaskUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(
    listTabGroup : List<TaskGroupUiState>,
    taskDelegate: TaskDelegate,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    uiState: NewTaskUiState,
    onEvent: (HomeEvent) -> Unit,
) {
    var menuListButtonSheet by remember{mutableStateOf<List<AppMenuItem>?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        floatingActionButton = {
            AppFloatActionButton{ if (taskDelegate.currentCollectionId() > 0) onEvent(HomeEvent.ShowAddTaskSheet) }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            PagerTabLayout( listTabGroup, taskDelegate, navController)
        }

        if(uiState.isAddTaskSheetVisible) {
            AddTaskBottomSheet(uiState = uiState, onEvent = onEvent,)
        }

        if(menuListButtonSheet.isNullOrEmpty()== false) {
            ModalBottomSheet(
                onDismissRequest = {
                    menuListButtonSheet = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    menuListButtonSheet?.forEach { item ->
                        Text(item.title, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.action.invoke()
                                menuListButtonSheet = null
                            }
                            .padding(12.dp))
                    }
                }
            }
        }
    }
}