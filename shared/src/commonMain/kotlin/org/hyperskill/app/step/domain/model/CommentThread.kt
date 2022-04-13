package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
enum class CommentThread {
    @SerialName("comment")
    COMMENT,
    @SerialName("contribute")
    CONTRIBUTE,
    @SerialName("feedback")
    FEEDBACK,
    @SerialName("reflection")
    REFLECTION,
    @SerialName("skip")
    SKIP,
    @SerialName("publication")
    PUBLICATION,
    @SerialName("quick introduction")
    QUICK_INTRODUCTION,
    @SerialName("review")
    REVIEW,
    @SerialName("tell about yourself")
    TELL_ABOUT_YOURSELF,
    @SerialName("survey about experience")
    SURVEY_ABOUT_EXPERIENCE,
    @JsonNames("solutions", "solution")
    SOLUTIONS,
    @SerialName("hint")
    HINT,
    @SerialName("useful link")
    USEFUL_LINK,
    @SerialName("financial aid")
    FINANCIAL_AID,
    @SerialName("typo report")
    TYPO_REPORT,
    @SerialName("suggest problem")
    SUGGEST_PROBLEM,
    @SerialName("milestone")
    MILESTONE,
    @SerialName("poll")
    POLL
}