package com.nguyenminhkhang.taskmanagement.ui.splash
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyenminhkhang.shared.usecase.auth.ObserveAuthStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SplashUiState(
	val isLoading: Boolean = true,
	val isLoggedIn: Boolean? = null
)

class SplashScreenViewModel(
	private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow(SplashUiState())
	val uiState = _uiState.asStateFlow()

	init {
		viewModelScope.launch {
			val currentUser = observeAuthStateUseCase().first()
			_uiState.value = SplashUiState(
				isLoading = false,
				isLoggedIn = currentUser != null
			)
		}
	}
}