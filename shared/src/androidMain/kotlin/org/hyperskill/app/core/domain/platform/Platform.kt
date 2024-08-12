package org.hyperskill.app.core.domain.platform

actual class Platform actual constructor() {
    actual val platformType: PlatformType = PlatformType.ANDROID
    actual val platformDescription: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual val analyticName: String = "android"

    actual val feedbackName: String = "Android"
}