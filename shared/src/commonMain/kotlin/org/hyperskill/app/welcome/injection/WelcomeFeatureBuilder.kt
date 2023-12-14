package org.hyperskill.app.welcome.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor
import org.hyperskill.app.welcome.presentation.WelcomeActionDispatcher
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Action
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Message
import org.hyperskill.app.welcome.presentation.WelcomeFeature.State
import org.hyperskill.app.welcome.presentation.WelcomeReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object WelcomeFeatureBuilder {
    private const val LOG_TAG = "WelcomeFeature"

    fun build(
        welcomeInteractor: WelcomeInteractor,
        currentProfileStateRepository: CurrentProfileStateRepository,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val welcomeReducer = WelcomeReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)

        val welcomeActionDispatcher = WelcomeActionDispatcher(
            ActionDispatcherOptions(),
            welcomeInteractor,
            currentProfileStateRepository,
            analyticInteractor
        )

        return ReduxFeature(State.Idle, welcomeReducer)
            .wrapWithActionDispatcher(welcomeActionDispatcher)
    }
}