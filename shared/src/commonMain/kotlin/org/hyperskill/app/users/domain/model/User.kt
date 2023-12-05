package org.hyperskill.app.users.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Long,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("fullname")
    val fullname: String
)