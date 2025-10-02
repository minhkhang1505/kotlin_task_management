package com.nguyenminhkhang.taskmanagement.domain.model

enum class SortedType(val value: Int) {
    SORTED_BY_FAVORITE(1),
    SORTED_BY_DATE(0)
}

fun Int.toSortType() : SortedType {
    return when (this) {
        1-> SortedType.SORTED_BY_FAVORITE
        else -> SortedType.SORTED_BY_DATE
    }
}
