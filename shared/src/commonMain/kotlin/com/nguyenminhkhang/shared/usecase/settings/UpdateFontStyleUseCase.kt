package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.repository.SettingsRepository

class UpdateFontStyleUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(fontStyle: String) {
        settingsRepository.setFontStyle(fontStyle)
    }
}