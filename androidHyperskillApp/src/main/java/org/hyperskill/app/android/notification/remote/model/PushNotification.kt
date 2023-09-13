package org.hyperskill.app.android.notification.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushNotification(
    @SerialName("title")
    val title: String,
    @SerialName("body")
    val body: String,
    @SerialName("image")
    val image: String? = null
)