package org.hyperskill.app

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual val analyticName: String = "android"
}