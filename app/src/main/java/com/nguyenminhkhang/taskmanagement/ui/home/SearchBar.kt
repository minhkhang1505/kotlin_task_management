package com.nguyenminhkhang.taskmanagement.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.ui.home.state.SearchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    searchResult: List<TaskEntity>,
    searchState: SearchState,
    onEvent: (HomeEvent) -> Unit,
) {
    SearchBar(
        modifier = Modifier.fillMaxWidth(), // Không cần các modifier khác
        query = searchState.searchQuery,
        onQueryChange = { onEvent(HomeEvent.Search(SearchEvent.OnSearchQueryChange(it))) },
        onSearch = { onEvent(HomeEvent.Search(SearchEvent.CollapseSearchBar)) },
        active = searchState.expanded, // Đổi 'expanded' thành 'active' cho đúng ngữ nghĩa
        onActiveChange = { onEvent(HomeEvent.Search(SearchEvent.ToggleSearchBarVisibility)) },

        // <<< THIẾT KẾ NÚT BACK Ở ĐÂY >>>
        leadingIcon = {
            IconButton(onClick = {
                // Khi bấm nút back, gửi event để đóng SearchBar
                onEvent(HomeEvent.Search(SearchEvent.ExpandSearchBarChanged))
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },

        placeholder = { Text("Search tasks...") },
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
                        .clickable { onEvent(HomeEvent.Search(SearchEvent.OnSearchResultClick(result.id!!))) }
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