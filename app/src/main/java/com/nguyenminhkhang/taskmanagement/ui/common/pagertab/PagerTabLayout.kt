package com.nguyenminhkhang.taskmanagement.ui.common.pagertab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.common.components.CustomInputTextField
import com.nguyenminhkhang.taskmanagement.ui.home.event.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.ID_ADD_NEW_LIST
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.CollectionEvent
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateToTaskDetail: (Long) -> Unit
) {
    var pageCount by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { pageCount })
    var internalState by remember {
        mutableStateOf(state.listTabGroup)
    }
    internalState = state.listTabGroup
    pageCount = state.listTabGroup.count{
        it.tab.id != ID_ADD_NEW_LIST
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            internalState.getOrNull(index)?.tab?.id?.let { currentCollectionId ->
                onEvent(CollectionEvent.CurrentCollectionId(currentCollectionId))
            }
        }
    }

    // TabBar
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        AppTabRowLayout(
            selectedTabIndex = pagerState,
            listTabs = state.listTabGroup.map{ it.tab },
            onTabSelected = {index ->
                if(( state.listTabGroup.getOrNull(index)?.tab?.id ?: 0) == ID_ADD_NEW_LIST) {
                    onEvent(UiEvent.ShowAddNewCollectionButton)
                } else {
                    scope.launch {
                        pagerState.scrollToPage(index)
                    }
                }
            }
        )
    }

    HorizontalPager(
        pagerState, key = { it }, beyondViewportPageCount = 2
    ) { pageIndex ->
        TaskListPage(state,state = state.listTabGroup[pageIndex], onEvent, onNavigateToTaskDetail)
    }

    if(state.isShowAddNewCollectionSheetVisible) {

        ModalBottomSheet({
            onEvent(UiEvent.HideAddNewCollectionButton)
        }) {
            Text(
                stringResource(R.string.input_task_collection_name),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())

            CustomInputTextField(
                value = state.newTaskCollectionName,
                onValueChange = { newValue -> onEvent(CollectionEvent.OnCollectionNameChanged(newValue)) },
                placeholderDescription = "Enter new collection name"
            )

            Button(
                onClick = {
                    onEvent(CollectionEvent.AddNewCollectionRequested(state.newTaskCollectionName))
                    onEvent(UiEvent.HideAddNewCollectionButton)
                    onEvent(CollectionEvent.NewCollectionNameCleared)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) { Text(stringResource(R.string.add_new_collecion)) }
        }
    }
}