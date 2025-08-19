package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    searchResult: List<TaskEntity>,
    searchState: SearchUiState,
    onEvent: (SearchEvent) -> Unit,
) {
    SearchBar(
        modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(12.dp)),
        query = searchState.searchQuery,
        onQueryChange = { onEvent(SearchEvent.OnSearchQueryChange(it)) },
        onSearch = { onEvent(SearchEvent.CollapseSearchBar) },
        active = searchState.expanded,
        onActiveChange = { isExpanded -> onEvent(SearchEvent.OnExpandedChange(isExpanded)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
        placeholder = { Text("Search tasks...") },
        trailingIcon = {
            if (searchState.expanded && searchState.searchQuery.isNotEmpty()) {
                IconButton(onClick = { SearchEvent.ClearSearchQuery }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search query")
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Today tasks",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            searchResult.forEach {result ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { SearchEvent.OnSearchResultClick(result.id!!) }
                ) {
                    Text(
                        text = result.content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}