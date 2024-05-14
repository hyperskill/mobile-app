package org.hyperskill.app.main.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent

/**
 * Represents first time app launch analytic event.
 *
 * @see AppsFlyerAnalyticEvent
 */
object AppLaunchFirstTimeAppsFlyerAnalyticEvent : AppsFlyerAnalyticEvent("af_app_launched")