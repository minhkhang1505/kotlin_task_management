package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.repository.SettingsRepository

class UpdateLanguageUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(languageCode: String) {
        settingsRepository.setLanguage(languageCode)
    }
}