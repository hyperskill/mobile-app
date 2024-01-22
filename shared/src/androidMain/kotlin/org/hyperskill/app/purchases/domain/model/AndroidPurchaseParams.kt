package org.hyperskill.app.purchases.domain.model

import android.app.Activity

data class AndroidPurchaseParams(
    val activity: Activity
) : PlatformPurchaseParams