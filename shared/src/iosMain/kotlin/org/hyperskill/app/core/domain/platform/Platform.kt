package org.hyperskill.app.core.domain.platform

import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platformType: PlatformType = PlatformType.IOS
    actual val platformDescription: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual val analyticName: String = "ios"

    actual val feedbackName: String = "iOS"
}