package org.hyperskill.app.core.domain.platform

import dev.icerock.moko.resources.StringResource

expect class Platform() {
    val platformType: PlatformType
    val platformDescription: String

    val analyticName: String

    val feedbackName: String

    val appNameResource: StringResource

    val isSubscriptionPurchaseEnabled: Boolean
}