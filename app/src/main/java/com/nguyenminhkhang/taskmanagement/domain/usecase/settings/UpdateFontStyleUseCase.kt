package com.nguyenminhkhang.taskmanagement.domain.usecase.settings

import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateFontStyleUseCase (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(fontStyle: String) {
        settingsRepository.setFontStyle(fontStyle)
    }
}