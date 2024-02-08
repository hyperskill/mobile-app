package org.hyperskill.app.paywall.domain.model

interface PaywallRepository {
    fun getSessionCountSinceLastPaywallShowed(): Int

    fun incrementSessionCountSinceLastPaywallShowed()

    fun resetSessionCountSinceLastPaywallShowed()
}