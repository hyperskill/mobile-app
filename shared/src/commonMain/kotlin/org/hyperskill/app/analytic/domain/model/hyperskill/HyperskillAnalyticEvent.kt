package org.hyperskill.app.analytic.domain.model.hyperskill

import kotlinx.datetime.Clock
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticKeys
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a Hyperskill analytic event.
 *
 * @property route Event route path, for example: `/home`, `/onboarding`.
 * @property action Event action, for example: `click`, `view`.
 * @property part Event part where action occurred, for example: `main`, `step_hints`.
 * @property target Target that triggered event, for example: `send`, `refresh`.
 * @property context Context that describes an event, for example: `stepId`, `notificationId`.
 * @see AnalyticEvent
 * @see HyperskillAnalyticRoute
 * @see HyperskillAnalyticAction
 * @see HyperskillAnalyticPart
 * @see HyperskillAnalyticTarget
 */
abstract class HyperskillAnalyticEvent(
    val route: HyperskillAnalyticRoute,
    val action: HyperskillAnalyticAction,
    val part: HyperskillAnalyticPart? = null,
    val target: HyperskillAnalyticTarget? = null,
    val context: Map<String, Any>? = null
) : AnalyticEvent {

    private val clientTime = Clock.System.now()

    override val name: String = this::class.simpleName ?: ""

    @Deprecated("Use context in constructor instead!")
    override val params: Map<String, Any> =
        mapOfNotNull(
            AnalyticKeys.PARAM_CLIENT_TIME to clientTime.toString(),
            AnalyticKeys.PARAM_ROUTE to route.path,
            AnalyticKeys.PARAM_ACTION to action.actionName,
            AnalyticKeys.PARAM_PART to part?.partName,
            AnalyticKeys.PARAM_TARGET to target?.targetName,
            AnalyticKeys.PARAM_CONTEXT to context?.takeIf { it.isNotEmpty() }
        )

    final override val sources: Set<AnalyticSource>
        get() = setOf(
            AnalyticSource.HYPERSKILL_API,
            AnalyticSource.AMPLITUDE
        )
}