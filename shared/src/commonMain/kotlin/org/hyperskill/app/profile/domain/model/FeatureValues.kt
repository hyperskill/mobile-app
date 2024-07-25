package org.hyperskill.app.profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeatureValues(
    @SerialName("mobile_content_trial_free_topics")
    val mobileContentTrialFreeTopics: Int = 5
)