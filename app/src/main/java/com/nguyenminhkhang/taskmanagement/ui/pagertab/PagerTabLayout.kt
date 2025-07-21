package com.nguyenminhkhang.taskmanagement.ui.pagertab

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
import com.nguyenminhkhang.taskmanagement.ui.home.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerTabLayout(state: List<TaskGroupUiState>, taskDelegate: TaskDelegate, onEvent: (HomeEvent) -> Unit, navController: NavController) {
    var pageCount by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { pageCount })
    var internalState by remember {
        mutableStateOf(state)
    }
    internalState = state
    pageCount = state.count{
        it.tab.id != ID_ADD_NEW_LIST
    }
    val scope = rememberCoroutineScope()
    var isShowAddNewCollectionButton by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            internalState.getOrNull(index)?.tab?.id?.let { currentCollectionId ->
                taskDelegate.updateCurrentCollectionId(currentCollectionId)
            }
        }
    }

    AppTabRowLayout(
        selectedTabIndex = pagerState.currentPage,
        listTabs = state.map{ it.tab },
        onTabSelected = {index ->
            if(( state.getOrNull(index)?.tab?.id ?: 0) == ID_ADD_NEW_LIST) {
                isShowAddNewCollectionButton = true
                taskDelegate.requestAddNewCollection()
            } else {
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        }
    )

    HorizontalPager(
        pagerState, key = { it }, beyondViewportPageCount = 2
    ) { pageIndex ->
        TaskListPage(state = state[pageIndex], onEvent, navController)
    }

    if(isShowAddNewCollectionButton) {
        var inputTaskCollection by remember { mutableStateOf("") }

        ModalBottomSheet({
            isShowAddNewCollectionButton = false
        }) {
            Text("Input task Collection",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())
            TextField(value=inputTaskCollection, onValueChange = { inputTaskCollection = it },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth())

            Button({
                if(inputTaskCollection.isNotEmpty()) {
                    taskDelegate.addNewCollection(inputTaskCollection)
                    inputTaskCollection = ""
                }
                isShowAddNewCollectionButton = false
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                Text("Add collection")
            }
        }
    }
}