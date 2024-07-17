package org.hyperskill.app.reactions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReactionType {
    @SerialName(":smile:")
    SMILE,
    @SerialName(":+1:")
    PLUS,
    @SerialName(":-1:")
    MINUS,
    @SerialName(":confused:")
    CONFUSED,
    @SerialName(":thinking:")
    THINKING,
    @SerialName(":fire:")
    FIRE,
    @SerialName(":clap:")
    CLAP,

    @SerialName(":helpful:")
    HELPFUL,
    @SerialName(":unhelpful:")
    UNHELPFUL,

    UNKNOWN
}

val ReactionType.Companion.commentReactions: Set<ReactionType>
    get() = setOf(
        ReactionType.PLUS,
        ReactionType.MINUS,
        ReactionType.SMILE,
        ReactionType.CONFUSED,
        ReactionType.THINKING,
        ReactionType.FIRE,
        ReactionType.CLAP
    )

internal val ReactionType.Companion.hintReactions: Set<ReactionType>
    get() = setOf(ReactionType.HELPFUL, ReactionType.UNHELPFUL)