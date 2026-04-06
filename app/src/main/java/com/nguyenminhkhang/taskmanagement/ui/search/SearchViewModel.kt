package com.nguyenminhkhang.taskmanagement.ui.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.core.time.TimeProvider
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.data.mapper.toEntity
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val analyticsTracker: AnalyticsTracker,
    private val timeProvider: TimeProvider
) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<TaskEntity>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.length < 2) {
                flowOf(emptyList())
            } else {
                taskRepository.SearchTasks(query)
                    .map { tasks -> tasks.map { it.toEntity() } }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getTodayTasks()
    }

    private fun getTodayTasks() {
        val now = Instant.ofEpochMilli(timeProvider.getCurrentTimeMillis())
        val today = now.atZone(ZoneId.systemDefault()).toLocalDate()
        val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000 - 1

        viewModelScope.launch {
            taskRepository.getTodayTasks(startOfDay, endOfDay)
                .map { tasks -> tasks.map { it.toEntity() } }
                .collect { todayTasks ->
                    _searchUiState.update { it.copy(todayTaskResult = todayTasks) }
                }
        }
    }

    fun onScreenShow() {
        analyticsTracker.trackEvent(
            AnalyticsEvent.ScreenView("SearchScreen")
        )
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearchQueryChange -> {
                _searchUiState.update { it.copy(searchQuery = event.query) }
                _searchQuery.value = event.query
            }
            is SearchEvent.OnExpandedChange -> {
                _searchUiState.update { it.copy(expanded = event.isExpanded) }
                if (!event.isExpanded) {
                    _searchUiState.update { it.copy(searchQuery = "") }
                    _searchQuery.value = ""
                }
            }
            is SearchEvent.OnSearchResultClick -> {}
            is SearchEvent.ToggleSearchBarVisibility -> { _searchUiState.update { it.copy(isSearchBarVisible = !it.isSearchBarVisible) } }
            is SearchEvent.ClearSearchQuery -> {
                _searchUiState.update { it.copy(searchQuery = "") }
                _searchQuery.value = ""
            }
            is SearchEvent.HideSearchBar -> { _searchUiState.update { it.copy(isSearchBarVisible = false) } }
            is SearchEvent.ExpandSearchBarChanged -> { _searchUiState.update { it.copy(expanded = !it.expanded) } }
            is SearchEvent.CollapseSearchBar -> { _searchUiState.update { it.copy(expanded = false) } }
            is SearchEvent.OnToggleFavoriteClick -> {
                viewModelScope.launch {
                    taskRepository.updateTaskFavoriteById(event.taskId, event.isFavorite)
                }
            }
        }
    }
}