package org.hyperskill.app.profile.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.products.domain.interactor.ProductsInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.presentation.ProfileActionDispatcher
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import org.hyperskill.app.profile.presentation.ProfileReducer
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProfileFeatureBuilder {
    fun build(
        profileInteractor: ProfileInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        streaksInteractor: StreaksInteractor,
        productsInteractor: ProductsInteractor,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        notificationInteractor: NotificationInteractor,
        urlPathProcessor: UrlPathProcessor,
        streakFlow: StreakFlow,
        dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow
    ): Feature<State, Message, Action> {
        val profileReducer = ProfileReducer()
        val profileActionDispatcher = ProfileActionDispatcher(
            ActionDispatcherOptions(),
            profileInteractor,
            currentProfileStateRepository,
            streaksInteractor,
            productsInteractor,
            analyticInteractor,
            sentryInteractor,
            notificationInteractor,
            urlPathProcessor,
            streakFlow,
            dailyStudyRemindersEnabledFlow
        )

        return ReduxFeature(State.Idle, profileReducer)
            .wrapWithActionDispatcher(profileActionDispatcher)
    }
}