package com.nguyenminhkhang.taskmanagement.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object AccountRoute
@Serializable
data object HomeRoute
@Serializable
data object FontStyleRoute
@Serializable
data object LanguageRoute
@Serializable
data object RegisterRoute
@Serializable
data class RepeatRoute(val taskId: Long)
@Serializable
data object SearchRoute
@Serializable
data object SignInRoute
@Serializable
data class TaskDetailRoute(val taskId: Long)
@Serializable
data object ThemeRoute
