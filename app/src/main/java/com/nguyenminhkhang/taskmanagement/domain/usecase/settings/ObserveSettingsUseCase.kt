package com.nguyenminhkhang.taskmanagement.domain.usecase.settings

import com.nguyenminhkhang.shared.model.SettingsPreferences
import com.nguyenminhkhang.taskmanagement.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase (
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<SettingsPreferences> = settingsRepository.settingsFlow
}