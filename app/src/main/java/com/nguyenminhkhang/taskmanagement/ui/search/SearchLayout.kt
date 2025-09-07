package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchLayout(
//    searchUiState: SearchUiState,
//    searchResults: List<TaskEntity>,
//    onEvent: (SearchEvent) -> Unit,
//) {
//    SearchBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = if (searchUiState.expanded) 0.dp else 16.dp), // Full width when active
//        query = searchUiState.searchQuery,
//        onQueryChange = { onEvent(SearchEvent.OnSearchQueryChange(it)) },
//        onSearch = { onEvent(SearchEvent.CollapseSearchBar) },
//        active = searchUiState.expanded,
//        onActiveChange = { isExpanded -> onEvent(SearchEvent.OnExpandedChange(isExpanded)) },
//        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
//        placeholder = { Text("Search tasks...") },
//        trailingIcon = {
//            if (searchUiState.searchQuery.isNotEmpty()) {
//                IconButton(onClick = { onEvent(SearchEvent.ClearSearchQuery) }) {
//                    Icon(Icons.Default.Clear, contentDescription = "Clear search query")
//                }
//            }
//        },
//        colors = SearchBarDefaults.colors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainer,
//        ),
//    ) {
//        SearchContent(
//            searchResults = searchResults,
//            searchQuery = searchUiState.searchQuery
//        )
//    }
//
//    DefaultContent(
//        todayTasks = searchUiState,
//        onEvent = onEvent
//    )
//}

@Composable
private fun SearchContent(searchResults: List<TaskEntity>, searchQuery: String) {
    if (searchQuery.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(WindowInsets.ime.asPaddingValues()).padding(16.dp), contentAlignment = Alignment.TopCenter) {
            Text("Try searching for 'Project X' or 'Meeting'", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        val imePadding = WindowInsets.ime.asPaddingValues()

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = imePadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (searchResults.isEmpty()) {
                item {
                    Text(
                        text = "No tasks found",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                items(searchResults, key = { task -> task.id ?: 0 }) { result ->
                    SearchTaskItemLayout(
                        taskResult = result,
                        onTaskClick = {}
                    )
                }
            }
        }
    }
}

/**
 * Composable for displaying the default screen content when search is INACTIVE.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
    searchUiState: SearchUiState,
    searchResults: List<TaskEntity>,
    onEvent: (SearchEvent) -> Unit,
) {
    // ✨ Sử dụng Column để chứa cả DockedSearchBar và nội dung mặc định
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✨ Bước 1: Thay thế bằng DockedSearchBar
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Luôn có padding
            query = searchUiState.searchQuery,
            onQueryChange = { onEvent(SearchEvent.OnSearchQueryChange(it)) },
            onSearch = { onEvent(SearchEvent.CollapseSearchBar) },
            active = searchUiState.expanded,
            onActiveChange = { isExpanded -> onEvent(SearchEvent.OnExpandedChange(isExpanded)) },
            leadingIcon = {
                if (searchUiState.expanded) {
                    IconButton(onClick = {
                        onEvent(SearchEvent.CollapseSearchBar)
                        onEvent(SearchEvent.ClearSearchQuery)
                    }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Close search", modifier = Modifier.size(34.dp) )
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search icon")
                }
            },
            placeholder = { Text("Search tasks...") },
            trailingIcon = {
                if (searchUiState.searchQuery.isNotEmpty()) {
                    Box(
                        modifier = Modifier.padding(4.dp).height(42.dp).clip( shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 22.dp, bottomEnd = 8.dp, topEnd = 22.dp) ).background(
                            color = MaterialTheme.colorScheme.onPrimary.copy(0.7f),
                            shape = MaterialTheme.shapes.small
                        ).clickable(
                            onClick = { onEvent(SearchEvent.ClearSearchQuery) }
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Clear", modifier = Modifier.padding(6.dp))
                    }
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        ) {
            // Nội dung tìm kiếm vẫn giữ nguyên, nhưng giờ nó sẽ hiển thị trong một vùng drop-down
            SearchContent(
                searchResults = searchResults,
                searchQuery = searchUiState.searchQuery
            )
        }

        // ✨ Bước 2: Hiển thị DefaultContent khi search bar KHÔNG active
        // AnimatedVisibility giúp hiệu ứng chuyển cảnh mượt mà hơn
        AnimatedVisibility(!searchUiState.expanded) {
            DefaultContent(
                todayTasks = searchUiState,
                onEvent = onEvent
            )
        }
    }
}


@Composable
private fun DefaultContent(
    todayTasks: SearchUiState,
    onEvent: (SearchEvent) -> Unit
) {
    // Xóa padding top vì bây giờ nó nằm trong một Column có quản lý vị trí
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp), // Tăng padding top một chút
    ) {
        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (todayTasks.todayTaskResult.isEmpty()) {
            Text(
                text = "No tasks for today!",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            TodayTaskList(searchUiState = todayTasks, onEvent = {})
        }
    }
}

//@Composable
//fun SearchLayout(
//    searchResults: List<TaskEntity>,
//    searchUiState: SearchUiState,
//    onEvent: (SearchEvent) -> Unit,
//) {
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//    ) {
//        Text("Search Page", style = MaterialTheme.typography.titleLarge )
//        CustomSearchBar(
//            searchResult = searchResults,
//            searchState = searchUiState,
//            onEvent = onEvent
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        Text(
//            text = "Today's Tasks",
//            style = MaterialTheme.typography.titleMedium,
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        if (searchUiState.todayTaskResult.isEmpty()) {
//            Text(
//                text = "No tasks found",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        } else {
//            TodayTaskList(
//                searchUiState = searchUiState,
//                onEvent = {}
//            )
//        }
//    }
//}