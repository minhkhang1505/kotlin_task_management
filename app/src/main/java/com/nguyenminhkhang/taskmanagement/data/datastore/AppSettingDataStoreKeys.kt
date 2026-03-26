package com.nguyenminhkhang.taskmanagement.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object AppSettingDataStoreKeys {
    val IS_NOTIFICATION_ON = booleanPreferencesKey("is_notification_on")
}