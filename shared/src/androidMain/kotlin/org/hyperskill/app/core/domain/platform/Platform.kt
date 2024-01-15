package org.hyperskill.app.core.domain.platform

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources

actual class Platform actual constructor() {
    actual val platformType: PlatformType = PlatformType.ANDROID
    actual val platformDescription: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    actual val analyticName: String = "android"

    actual val feedbackName: String = "Android"

    actual val appNameResource: StringResource = SharedResources.strings.android_app_name
}