package org.hyperskill.app.likes.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LikeSubject {
    @SerialName("clarity")
    CLARITY,
    @SerialName("fun")
    FUN,
    @SerialName("skip")
    SKIP,
    @SerialName("abuse")
    ABUSE,
    @SerialName("cheatsheet")
    CHEATSHEET,
    @SerialName("")
    DEFAULT,
    @SerialName("comment")
    COMMENT,
    @SerialName("rating")
    RATING
}