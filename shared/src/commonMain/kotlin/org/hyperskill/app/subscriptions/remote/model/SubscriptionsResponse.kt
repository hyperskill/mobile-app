package org.hyperskill.app.subscriptions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.subscriptions.domain.model.Subscription

@Serializable
class SubscriptionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("subscriptions")
    val subscriptions: List<Subscription>
) : MetaResponse