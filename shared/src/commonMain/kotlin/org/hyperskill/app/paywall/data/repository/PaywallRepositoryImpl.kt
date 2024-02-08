package org.hyperskill.app.paywall.data.repository

import org.hyperskill.app.paywall.data.source.PaywallCacheDataSource
import org.hyperskill.app.paywall.data.source.incrementLastPaywallShowedSessionCount
import org.hyperskill.app.paywall.data.source.resetLastPaywallShowedSessionCount
import org.hyperskill.app.paywall.domain.model.PaywallRepository

class PaywallRepositoryImpl(
    private val paywallCacheDataSource: PaywallCacheDataSource
) : PaywallRepository {
    override fun getSessionCountSinceLastPaywallShowed(): Int =
        paywallCacheDataSource.getLastPaywallShowedSessionCount()

    override fun incrementSessionCountSinceLastPaywallShowed() =
        paywallCacheDataSource.incrementLastPaywallShowedSessionCount()

    override fun resetSessionCountSinceLastPaywallShowed() =
        paywallCacheDataSource.resetLastPaywallShowedSessionCount()
}