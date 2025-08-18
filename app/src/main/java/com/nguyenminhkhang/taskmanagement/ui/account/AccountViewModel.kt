package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.lifecycle.ViewModel
import com.nguyenminhkhang.taskmanagement.ui.account.state.AccountUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()


}