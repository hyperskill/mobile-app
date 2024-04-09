package org.hyperskill.app.submissions.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.submissions.domain.serialization.score.ReplyScoreContentSerializer
import org.hyperskill.app.submissions.domain.serialization.score.ReplyScoreIntSerializer
import org.hyperskill.app.submissions.domain.serialization.score.ReplyScoreStringSerializer

@Serializable(with = ReplyScoreContentSerializer::class)
sealed interface ReplyScore {
    @Serializable(with = ReplyScoreStringSerializer::class)
    data class String(val stringValue: kotlin.String) : ReplyScore

    @Serializable(with = ReplyScoreIntSerializer::class)
    data class Int(val intValue: kotlin.Int) : ReplyScore
}