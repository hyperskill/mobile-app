package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class ProfileSettingsClickedHyperskillAnalyticEvent(
    part: HyperskillAnalyticPart = HyperskillAnalyticPart.MAIN,
    target: HyperskillAnalyticTarget
) : HyperskillAnalyticEvent(HyperskillAnalyticRoute.Profile.Settings(), HyperskillAnalyticAction.CLICK, part, target)