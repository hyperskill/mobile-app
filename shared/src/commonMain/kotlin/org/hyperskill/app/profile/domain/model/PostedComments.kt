package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostedComments(
    @SerialName("comment")
    val comment: Long? = null,
    @SerialName("hint")
    val hint: Long? = null,
    @SerialName("useful link")
    val usefulLink: Long? = null,
    @SerialName("solutions")
    val solutions: Long? = null
)
