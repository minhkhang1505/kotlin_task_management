package com.nguyenminhkhang.taskmanagement.domain.usecase.settings

import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateColorThemeUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(colorTheme: String) {
        settingsRepository.setColorTheme(colorTheme)
    }
}