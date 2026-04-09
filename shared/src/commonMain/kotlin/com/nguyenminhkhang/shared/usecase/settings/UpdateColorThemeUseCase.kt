package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.repository.SettingsRepository

class UpdateColorThemeUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(colorTheme: String) {
        settingsRepository.setColorTheme(colorTheme)
    }
}