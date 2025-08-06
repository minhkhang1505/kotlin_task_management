package com.nguyenminhkhang.taskmanagement.ui.home

sealed class SearchEvent {
    data class OnSearchQueryChange(val query: String) : SearchEvent()
    data class OnExpandedChange(val isExpanded: Boolean) : SearchEvent()
    data class OnSearchResultClick(val taskResultId: Long) : SearchEvent()
    object ToggleSearchBarVisibility : SearchEvent()
    object ClearSearchQuery : SearchEvent()
    object HideSearchBar : SearchEvent()
    object ExpandSearchBarChanged : SearchEvent()
    object CollapseSearchBar : SearchEvent()
}