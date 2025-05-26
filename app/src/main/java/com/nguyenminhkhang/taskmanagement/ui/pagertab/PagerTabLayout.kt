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
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import kotlinx.coroutines.launch

@Composable
fun PagerTabLayout(state: List<TaskGroupUiState>, taskDelegate: TaskDelegate) {
    var pageCount by remember { mutableStateOf(3) }
    val pagerState = rememberPagerState(pageCount = { state.size })

    pageCount = state.size
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }

            .collect { page ->
                taskDelegate.updateCurrentCollectionIndex(page)
        }
    }

    AppTabRowLayout(
        selectedTabIndex = pagerState.currentPage,
        listTabs = state.map{ it.tab },
        onTabSelected = {index ->
            Log.d("PagerTabLayout", "onTabSelected: $index")
            scope.launch {
                pagerState.scrollToPage(index)
            }
        }
    )

    HorizontalPager(
        pagerState, key = { it }, beyondViewportPageCount = 2
    ) { pageIndex ->
        TaskListPage(state = state[pageIndex].page, taskDelegate)
    }
}