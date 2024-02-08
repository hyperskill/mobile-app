package org.hyperskill.app.paywall.data.source

internal interface PaywallCacheDataSource {
    fun getLastPaywallShowedSessionCount(): Int

    fun setLastPaywallShowedSessionCount(count: Int)
}

internal fun PaywallCacheDataSource.incrementLastPaywallShowedSessionCount() {
    val currentCount = getLastPaywallShowedSessionCount()
    setLastPaywallShowedSessionCount(currentCount + 1)
}

internal fun PaywallCacheDataSource.resetLastPaywallShowedSessionCount() {
    setLastPaywallShowedSessionCount(0)
}