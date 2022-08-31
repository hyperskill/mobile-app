package org.hyperskill.app.analytic.domain.model.hyperskill

import kotlinx.datetime.Clock
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import ru.nobird.app.core.model.mapOfNotNull

open class HyperskillAnalyticEvent(
    val route: HyperskillAnalyticRoute,
    val action: HyperskillAnalyticAction,
    val part: HyperskillAnalyticPart? = null,
    val target: HyperskillAnalyticTarget? = null
) : AnalyticEvent {
    companion object {
        const val PARAM_CLIENT_TIME = "client_time"
        const val PARAM_ROUTE = "route"
        const val PARAM_ACTION = "action"
        const val PARAM_PART = "part"
        const val PARAM_TARGET = "target"
        const val PARAM_USER = "user"
        const val PARAM_CONTEXT = "context"
        const val PARAM_PLATFORM = "platform"
    }

    private val clientTime = Clock.System.now()

    override val name: String = ""

    override val params: Map<String, Any> =
        mapOfNotNull(
            PARAM_CLIENT_TIME to clientTime.toString(),
            PARAM_ROUTE to route.path,
            PARAM_ACTION to action.actionName,
            PARAM_PART to part?.partName,
            PARAM_TARGET to target?.targetName
        )

    override val source: AnalyticSource = AnalyticSource.HYPERSKILL_API
}