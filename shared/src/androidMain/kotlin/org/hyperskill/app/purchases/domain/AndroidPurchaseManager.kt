package org.hyperskill.app.purchases.domain

import android.content.Context
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import org.hyperskill.app.R
import org.hyperskill.app.purchases.domain.model.PurchaseManager

class AndroidPurchaseManager(
    private val context: Context
) : PurchaseManager {
    override fun setup() {
        Purchases.configure(
            PurchasesConfiguration.Builder(
                context = context,
                apiKey = context.getString(R.string.revenue_cat_api_key)
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