package org.hyperskill.app.problems_limit_info.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.step.domain.model.StepRoute

@Serializable
sealed interface ProblemsLimitInfoModalContext {
    val launchSource: ProblemsLimitInfoModalLaunchSource
    val analyticRoute: HyperskillAnalyticRoute

    @Serializable
    data class Step(
        val stepRoute: StepRoute,
        override val launchSource: ProblemsLimitInfoModalLaunchSource
    ) : ProblemsLimitInfoModalContext {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = stepRoute.analyticRoute
    }

    @Serializable
    data class GamificationToolbar(
        val gamificationToolbarScreen: GamificationToolbarScreen
    ) : ProblemsLimitInfoModalContext {
        override val launchSource: ProblemsLimitInfoModalLaunchSource
            get() = ProblemsLimitInfoModalLaunchSource.USER_INITIATED

        override val analyticRoute: HyperskillAnalyticRoute
            get() = gamificationToolbarScreen.analyticRoute
    }
}