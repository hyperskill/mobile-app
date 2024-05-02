package org.hyperskill.app.subscriptions.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import kotlinx.serialization.json.Json
import org.hyperskill.app.subscriptions.data.source.CurrentSubscriptionStateHolder
import org.hyperskill.app.subscriptions.domain.model.Subscription

internal class CurrentSubscriptionStateHolderImpl(
    private val json: Json,
    private val settings: Settings
) : CurrentSubscriptionStateHolder {

    private var cachedSubscription: Subscription? = null

    override suspend fun getState(): Subscription? {
        if (cachedSubscription == null) {
            cachedSubscription = readSubscriptionFromSettings()
        }
        return cachedSubscription
    }

    private fun readSubscriptionFromSettings(): Subscription? =
        if (settings.contains(SubscriptionCacheKeys.CURRENT_SUBSCRIPTION)) {
            json.decodeFromString(
                Subscription.serializer(),
                settings.getString(SubscriptionCacheKeys.CURRENT_SUBSCRIPTION)
            )
        } else {
            null
        }

    override suspend fun setState(newState: Subscription) {
        this.cachedSubscription = newState
        settings.putString(
            SubscriptionCacheKeys.CURRENT_SUBSCRIPTION,
            json.encodeToString(Subscription.serializer(), newState)
        )
    }

    override fun resetState() {
        this.cachedSubscription = null
        settings.remove(SubscriptionCacheKeys.CURRENT_SUBSCRIPTION)
    }
}