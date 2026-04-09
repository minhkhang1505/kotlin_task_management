package com.nguyenminhkhang.shared.usecase.settings

import com.nguyenminhkhang.shared.model.SettingsPreferences
import com.nguyenminhkhang.shared.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase (
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<SettingsPreferences> = settingsRepository.settingsFlow
}