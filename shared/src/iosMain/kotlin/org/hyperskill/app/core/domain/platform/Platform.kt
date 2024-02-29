package org.hyperskill.app.core.domain.platform

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platformType: PlatformType = PlatformType.IOS
    actual val platformDescription: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual val analyticName: String = "ios"

    actual val feedbackName: String = "iOS"

    actual val appNameResource: StringResource = SharedResources.strings.ios_app_name

    actual val isSubscriptionPurchaseEnabled: Boolean = false
}