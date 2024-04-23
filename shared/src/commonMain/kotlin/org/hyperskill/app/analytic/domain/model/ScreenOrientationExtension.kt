package org.hyperskill.app.analytic.domain.model

import org.hyperskill.app.core.domain.model.ScreenOrientation

val ScreenOrientation.analyticValue: String
    get() = when (this) {
        ScreenOrientation.PORTRAIT -> CommonAnalyticKeys.SCREEN_ORIENTATION_VALUE_PORTRAIT
        ScreenOrientation.LANDSCAPE -> CommonAnalyticKeys.SCREEN_ORIENTATION_VALUE_LANDSCAPE
    }