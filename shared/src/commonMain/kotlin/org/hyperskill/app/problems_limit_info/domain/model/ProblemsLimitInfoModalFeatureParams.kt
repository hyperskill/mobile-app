package org.hyperskill.app.problems_limit_info.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.subscriptions.domain.model.Subscription

@Serializable
data class ProblemsLimitInfoModalFeatureParams(
    val subscription: Subscription,
    val context: ProblemsLimitInfoModalContext
)