package com.nguyenminhkhang.taskmanagement.ui.search

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.taskmanagement.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.repository.TaskRepo
import com.nguyenminhkhang.taskmanagement.ui.search.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
val zoneId = ZoneId.systemDefault()
@RequiresApi(Build.VERSION_CODES.O)
val today = LocalDate.now(zoneId)

@RequiresApi(Build.VERSION_CODES.O)
val startOfDay = today.atStartOfDay(zoneId).toEpochSecond() * 1000

@RequiresApi(Build.VERSION_CODES.O)
val endOfDay = today.plusDays(1).atStartOfDay(zoneId).toEpochSecond() * 1000 - 1

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val taskRepo: TaskRepo,
) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState : StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<TaskEntity>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            if (query.length < 2) {
                flowOf(emptyList())
            } else {
                taskRepo.SearchTasks(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun getTodayTasks(startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.getTodayTasks(startDate, endDate).collect { todayTasks ->
                Log.d("SearchVM", "todayTasks=${todayTasks.size}")
                _searchUiState.update { it.copy(todayTaskResult = todayTasks) }
            }
        }
    }

    init {
        Log.d("SearchVM", "start=$startOfDay, end=$endOfDay")
        getTodayTasks(startOfDay, endOfDay)
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
            is SearchEvent.ClearSearchQuery -> { _searchUiState.update { it.copy(searchQuery = "") } }
            is SearchEvent.HideSearchBar -> { _searchUiState.update { it.copy(isSearchBarVisible = false) } }
            is SearchEvent.ExpandSearchBarChanged -> { _searchUiState.update { it.copy(expanded = !it.expanded) } }
            is SearchEvent.CollapseSearchBar -> { _searchUiState.update { it.copy(expanded = false) } }
        }
    }
}