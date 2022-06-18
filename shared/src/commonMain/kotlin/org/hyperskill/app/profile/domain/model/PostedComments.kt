package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostedComments(
    @SerialName("comment")
    val comment: Long,
    @SerialName("hint")
    val hint: Long,
    @SerialName("useful link")
    val usefulLink: Long,
    @SerialName("solutions")
    val solutions: Long
)
