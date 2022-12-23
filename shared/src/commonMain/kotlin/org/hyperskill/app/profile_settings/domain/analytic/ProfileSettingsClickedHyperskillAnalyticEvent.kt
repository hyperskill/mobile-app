package org.hyperskill.app.profile_settings.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a common click analytic event for the ProfileSettingsFeature.
 *
 * Click on the "Done" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "head",
 *     "target": "done"
 * }
 * ```
 *
 * Click on the "Theme" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "theme"
 * }
 * ```
 *
 * Click on the "JetBrains Academy Terms of Service" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "jetbrains_terms_of_service"
 * }
 * ```
 *
 * Click on the "Hyperskill Terms of Service and Privacy Policy" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "hyperskill_terms_of_service"
 * }
 * ```
 *
 * Click on the "Report a problem" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "report_a_problem"
 * }
 * ```
 *
 * Click on the "Sign out" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "sign_out"
 * }
 * ```
 *
 * Click on the "Delete account" button:
 * ```
 * {
 *     "route": "/profile/settings",
 *     "action": "click",
 *     "part": "main",
 *     "target": "delete_account"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class ProfileSettingsClickedHyperskillAnalyticEvent(
    part: HyperskillAnalyticPart = HyperskillAnalyticPart.MAIN,
    target: HyperskillAnalyticTarget
) : HyperskillAnalyticEvent(HyperskillAnalyticRoute.Profile.Settings(), HyperskillAnalyticAction.CLICK, part, target)