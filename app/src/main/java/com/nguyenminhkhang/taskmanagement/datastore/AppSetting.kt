package com.nguyenminhkhang.taskmanagement.datastore

import kotlinx.coroutines.flow.Flow

interface AppSetting {
    val appSettingFlow: Flow<AppSettingData>
    suspend fun setIsNotificationOn(isNotificationOn: Boolean)
    suspend fun getIsNotificationOn(): Boolean
}