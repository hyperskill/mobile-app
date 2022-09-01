package org.hyperskill.app

import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    actual val analyticName: String = "ios"

    actual val feedbackName: String = "iOS"
}