package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class ProfileSettingsDeleteAccountNoticeHiddenHyperskillAnalyticEvent(
    isDeleteConfirmed: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.DELETE_ACCOUNT_NOTICE,
    target = if (isDeleteConfirmed) HyperskillAnalyticTarget.DELETE else HyperskillAnalyticTarget.CANCEL
)