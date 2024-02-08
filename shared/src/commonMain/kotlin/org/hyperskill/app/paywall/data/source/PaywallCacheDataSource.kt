package org.hyperskill.app.paywall.data.source

interface PaywallCacheDataSource {
    fun getLastPaywallShowedSessionCount(): Int

    fun setLastPaywallShowedSessionCount(count: Int)
}

fun PaywallCacheDataSource.incrementLastPaywallShowedSessionCount() {
    val currentCount = getLastPaywallShowedSessionCount()
    setLastPaywallShowedSessionCount(currentCount + 1)
}

fun PaywallCacheDataSource.resetLastPaywallShowedSessionCount() {
    setLastPaywallShowedSessionCount(0)
}