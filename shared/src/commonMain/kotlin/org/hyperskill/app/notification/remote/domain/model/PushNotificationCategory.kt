package org.hyperskill.app.notification.remote.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PushNotificationCategory(val backendName: String?) {
    @SerialName("Activation")
    ACTIVATION("Activation"),
    @SerialName("Routine learning")
    ROUTINE_LEARNING("Routine learning"),
    @SerialName("Continue learning")
    CONTINUE_LEARNING("Continue learning"),
    @SerialName("Re-engagement")
    RE_ENGAGEMENT("Re-engagement"),

    UNKNOWN(null);

    companion object {
        fun getByBackendName(value: String): PushNotificationCategory =
            PushNotificationCategory.values().firstOrNull {
                it.backendName == value
            } ?: UNKNOWN
    }
}