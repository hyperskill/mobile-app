package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

object AmplitudeAnalyticEventMapper {
    private const val VIEW_STEP_ROUTE_TYPE_NAME = "step"

    fun map(analyticEvent: AnalyticEvent): AmplitudeAnalyticEvent? =
        when (analyticEvent) {
            is HyperskillAnalyticEvent -> AmplitudeAnalyticEvent(
                name = getType(analyticEvent),
                params = getParams(analyticEvent)
            )
            is AmplitudeAnalyticEvent -> analyticEvent
            else -> null
        }

    private fun getType(analyticEvent: HyperskillAnalyticEvent): String =
        buildString {
            append(analyticEvent.action.actionName)
            getTypeRoutePath(analyticEvent)?.let {
                append(' ')
                append(it)
            }
            if (analyticEvent.part != null) {
                append(' ')
                append(analyticEvent.part.partName)
            }
            if (analyticEvent.target != null) {
                append(' ')
                append(analyticEvent.target.targetName)
            }
        }

    private fun getTypeRoutePath(analyticEvent: HyperskillAnalyticEvent): String? =
        if (analyticEvent.action == HyperskillAnalyticAction.VIEW) {
            when (analyticEvent.route) {
                is HyperskillAnalyticRoute.Learn.Step,
                is HyperskillAnalyticRoute.Learn.Daily,
                is HyperskillAnalyticRoute.Projects.Stages.Implement,
                is HyperskillAnalyticRoute.Repeat.Step,
                is HyperskillAnalyticRoute.Repeat.Step.Theory ->
                    VIEW_STEP_ROUTE_TYPE_NAME

                is HyperskillAnalyticRoute.Repeat,
                is HyperskillAnalyticRoute.AppLaunchFirstTime,
                is HyperskillAnalyticRoute.Debug,
                is HyperskillAnalyticRoute.Home,
                is HyperskillAnalyticRoute.IosSpringBoard,
                is HyperskillAnalyticRoute.Leaderboard,
                is HyperskillAnalyticRoute.Login,
                is HyperskillAnalyticRoute.Onboarding,
                is HyperskillAnalyticRoute.Profile,
                is HyperskillAnalyticRoute.Progress,
                is HyperskillAnalyticRoute.Projects.SelectProjectDetails,
                is HyperskillAnalyticRoute.Search,
                is HyperskillAnalyticRoute.StudyPlan,
                is HyperskillAnalyticRoute.Tracks,
                HyperskillAnalyticRoute.Paywall,
                HyperskillAnalyticRoute.None ->
                    analyticEvent.route.path
            }
        } else {
            null
        }

    private fun getParams(analyticEvent: HyperskillAnalyticEvent): Map<String, Any> =
        buildMap {
            put(HyperskillAnalyticKeys.PARAM_ROUTE, analyticEvent.route.path)
            if (analyticEvent.context != null) {
                putAll(analyticEvent.context)
            }
        }
}