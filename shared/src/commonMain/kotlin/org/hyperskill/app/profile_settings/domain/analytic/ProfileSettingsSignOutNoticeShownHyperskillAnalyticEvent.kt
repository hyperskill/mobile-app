package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class ProfileSettingsSignOutNoticeShownHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.NOTICE,
    HyperskillAnalyticTarget.SIGN_OUT_NOTICE
)