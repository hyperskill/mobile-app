package org.hyperskill.app.purchases.domain.model

/**
 * Represents an interface that both platforms should implement.
 */
interface PurchaseManager {

    /**
     * Setups the payment sdk.
     * Must be called when the application is launched for the first time.
     */
    fun setup()

    /**
     * Identifies user in the payment sdk with provided [userId].
     * Must be called just after login event.
     */
    fun login(userId: Long)

    /**
     * Clear the user identification provided via [login].
     * Must be called just after logout event.
     */
    fun logout()
}