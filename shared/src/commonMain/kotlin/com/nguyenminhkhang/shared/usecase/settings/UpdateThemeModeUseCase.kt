package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.repository.SettingsRepository

class UpdateThemeModeUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(themeModeKey: String) {
        settingsRepository.setThemeMode(themeModeKey)
    }
}