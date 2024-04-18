package org.hyperskill.app.problems_limit_info.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.subscriptions.domain.model.Subscription

@Serializable
data class ProblemsLimitInfoModalFeatureParams(
    val subscription: Subscription,
    val profile: Profile,
    val stepRoute: StepRoute
)