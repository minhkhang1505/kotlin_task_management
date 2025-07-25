package com.nguyenminhkhang.taskmanagement.ui.pagertab

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ui.home.HomeEvent
import com.nguyenminhkhang.taskmanagement.ui.home.ID_ADD_NEW_LIST
import com.nguyenminhkhang.taskmanagement.ui.home.state.HomeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(state: HomeUiState, onEvent: (HomeEvent) -> Unit, navController: NavController) {
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
                onEvent(HomeEvent.CurrentCollectionId(currentCollectionId))
            }
        }
    }

    Row(
        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth()
    ) {
        AppTabRowLayout(
            selectedTabIndex = pagerState,
            listTabs = state.listTabGroup.map{ it.tab },
            onTabSelected = {index ->
                if(( state.listTabGroup.getOrNull(index)?.tab?.id ?: 0) == ID_ADD_NEW_LIST) {
                    onEvent(HomeEvent.ShowAddNewCollectionButton)
                    onEvent(HomeEvent.AddNewCollectionRequested)
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
        TaskListPage(state = state.listTabGroup[pageIndex], onEvent, navController)
    }

    if(state.isShowAddNewCollectionSheetVisible) {

        ModalBottomSheet({
            onEvent(HomeEvent.HideAddNewCollectionButton)
        }) {
            Text("Input task Collection",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())
            TextField(
                value = state.newTaskCollectionName,
                onValueChange = { newValue -> onEvent(HomeEvent.NewCollectionNameChanged(newValue)) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())

            Button(
                onClick = {
                    onEvent(HomeEvent.AddNewCollectionRequested)
                    onEvent(HomeEvent.HideAddNewCollectionButton)
                    onEvent(HomeEvent.NewCollectionNameCleared)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) { Text("Add collection") }
        }
    }
}