package org.hyperskill.app.submissions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubmissionStatus {
    @SerialName("evaluation")
    EVALUATION,
    @SerialName("wrong")
    WRONG,
    @SerialName("rejected")
    REJECTED,
    @SerialName("correct")
    CORRECT,
    @SerialName("outdated")
    OUTDATED,
    @SerialName("local")
    LOCAL
}

internal val SubmissionStatus?.isWrongOrRejected: Boolean
    get() = this != null && (this == SubmissionStatus.WRONG || this == SubmissionStatus.REJECTED)