package org.hyperskill.app.paywall.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.hyperskill.app.paywall.data.source.PaywallCacheDataSource

class PaywallCacheDataSourceImpl(
    private val settings: Settings
) : PaywallCacheDataSource {

    companion object {
        private const val LAST_PAYWALL_SHOWED_SESSION_COUNT_KEY = "LAST_PAYWALL_SHOWED_SESSION_COUNT_KEY"
    }

    override fun getLastPaywallShowedSessionCount(): Int  =
        settings.getInt(LAST_PAYWALL_SHOWED_SESSION_COUNT_KEY, defaultValue = 0)

    override fun setLastPaywallShowedSessionCount(count: Int) =
        settings.set(LAST_PAYWALL_SHOWED_SESSION_COUNT_KEY, count)
}