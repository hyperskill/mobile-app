package org.hyperskill.app.problems_limit_reached.injection

import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.subscriptions.domain.model.Subscription

data class ProblemsLimitReachedModalFeatureParams(
    val subscription: Subscription,
    val profile: Profile
)
