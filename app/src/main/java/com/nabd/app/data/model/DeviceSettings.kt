package com.nabd.app.data.model

data class DeviceSettings(
    val notificationPref: NotificationPreference = NotificationPreference.BOTH,
    val reminderInterval: Int = 15, // minutes
    val criticalAlertsEnabled: Boolean = true,
    val deviceEnabled: Boolean = true
)

enum class NotificationPreference {
    NOTIFICATION_ONLY, VIBRATION_ONLY, BOTH
}
