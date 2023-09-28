package org.hyperskill.app.notifications_onboarding.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingActionDispatcher
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object NotificationsOnboardingFeatureBuilder {
    private const val LOG_TAG = "NotificationsOnboardingFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        notificationInteractor: NotificationInteractor,
        buildVariant: BuildVariant,
        logger: Logger
    ): Feature<State, Message, Action> {
        val reducer = NotificationsOnboardingReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)
        val actionDispatcher = NotificationsOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor,
            notificationInteractor = notificationInteractor
        )
        return ReduxFeature(State, reducer).wrapWithActionDispatcher(actionDispatcher)
    }
}