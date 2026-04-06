package com.nguyenminhkhang.taskmanagement.domain.usecase.settings

import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(languageCode: String) {
        settingsRepository.setLanguage(languageCode)
    }
}