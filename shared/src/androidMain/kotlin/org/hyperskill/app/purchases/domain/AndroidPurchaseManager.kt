package org.hyperskill.app.purchases.domain

import android.content.Context
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.purchases.domain.model.PurchaseManager

class AndroidPurchaseManager(
    private val context: Context
) : PurchaseManager {
    override fun setup() {
        Purchases.configure(
            PurchasesConfiguration.Builder(
                context = context,
                apiKey = BuildConfig.REVENUE_CAT_GOOGLE_API_KEY
            ).build()
        )
    }

    override fun login(userId: Long) {
        Purchases.sharedInstance.logIn(userId.toString())
    }

    override fun logout() {
        Purchases.sharedInstance.logOut()
    }
}