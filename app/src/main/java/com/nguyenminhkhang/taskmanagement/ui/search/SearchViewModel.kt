package com.nguyenminhkhang.taskmanagement.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.domain.model.Task
import com.nguyenminhkhang.taskmanagement.domain.usecase.search.GetTodayTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.search.SearchTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel (
    private val searchTasksUseCase: SearchTasksUseCase,
    private val getTodayTasksUseCase: GetTodayTasksUseCase,
    private val toggleTaskFavoriteUseCase: ToggleTaskFavoriteUseCase,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Task>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.length < 2) {
                flowOf(emptyList())
            } else {
                searchTasksUseCase(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        observeTodayTasks()
    }

    private fun observeTodayTasks() {
        viewModelScope.launch {
            getTodayTasksUseCase().collect { todayTasks ->
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
                    toggleTaskFavoriteUseCase(event.taskId, event.isFavorite)
                }
            }
        }
    }
}