package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the prompt to sign out.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "hidden",
 *     "part": "sign_out_notice",
 *     "target": "yes / no"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileSettingsSignOutNoticeHiddenHyperskillAnalyticEvent(
    isLogoutConfirmed: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Profile.Settings(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.SIGN_OUT_NOTICE,
    target = if (isLogoutConfirmed) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
)