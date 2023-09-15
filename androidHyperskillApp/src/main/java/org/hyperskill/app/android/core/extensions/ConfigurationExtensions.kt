package org.hyperskill.app.android.core.extensions

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import org.hyperskill.app.core.domain.model.ScreenOrientation

val Configuration.screenOrientation: ScreenOrientation
    get() = if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
        ScreenOrientation.PORTRAIT
    } else {
        ScreenOrientation.LANDSCAPE
    }