package org.hyperskill.app

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual val isIos: Boolean = false
    actual val isAndroid: Boolean = true

    actual val analyticName: String = "android"

    actual val feedbackName: String = "Android"
}