package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class ProfileSettingsLogoutNoticeHiddenHyperskillAnalyticEvent(
    isLogoutConfirmed: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.LOGOUT_NOTICE,
    target = if (isLogoutConfirmed) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
)