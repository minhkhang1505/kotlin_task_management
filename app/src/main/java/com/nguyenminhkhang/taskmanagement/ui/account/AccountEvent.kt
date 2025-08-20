package com.nguyenminhkhang.taskmanagement.ui.account

sealed class AccountEvent {
    object SignOut : AccountEvent()
    object DismissLogoutDialog : AccountEvent()
    object ShowLogoutDialog : AccountEvent()
    object HideLogoutDialog : AccountEvent()
}