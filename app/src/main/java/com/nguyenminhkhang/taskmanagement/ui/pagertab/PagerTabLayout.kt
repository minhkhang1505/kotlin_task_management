package com.nguyenminhkhang.taskmanagement.ui.pagertab

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.ID_ADD_NEW_LIST
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import kotlinx.coroutines.launch

@Composable
fun PagerTabLayout(state: List<TaskGroupUiState>, taskDelegate: TaskDelegate, navController: NavController) {
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
        TaskListPage(state = state[pageIndex], taskDelegate, navController)
    }
}