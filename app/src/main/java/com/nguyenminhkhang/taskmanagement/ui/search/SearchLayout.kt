package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState
/**
 * Composable for displaying the default screen content when search is INACTIVE.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
    searchUiState: SearchUiState,
    searchResults: List<TaskEntity>,
    onEvent: (SearchEvent) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
            SearchContent(
                searchResults = searchResults,
                searchQuery = searchUiState.searchQuery,
                navController = navController
            )
        }

        AnimatedVisibility(!searchUiState.expanded) {
            DefaultContent(
                todayTasks = searchUiState,
                navController = navController,
            )
        }
    }
}