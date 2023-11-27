package org.hyperskill.app.leaderboards.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenActionDispatcher
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.ViewState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenReducer
import org.hyperskill.app.leaderboards.screen.view.mapper.LeaderboardScreenViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object LeaderboardScreenFeatureBuilder {
    private const val LOG_TAG = "LeaderboardScreenFeature"

    fun build(
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val leaderboardScreenReducer = LeaderboardScreenReducer(
            gamificationToolbarReducer = gamificationToolbarReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val leaderboardScreenActionDispatcher = LeaderboardScreenActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val leaderboardScreenViewStateMapper = LeaderboardScreenViewStateMapper()

        return ReduxFeature(
            initialState = LeaderboardScreenFeature.State(
                leaderboardState = LeaderboardScreenFeature.LeaderboardState.Idle,
                toolbarState = GamificationToolbarFeature.State.Idle
            ),
            reducer = leaderboardScreenReducer
        )
            .transformState(leaderboardScreenViewStateMapper::map)
            .wrapWithActionDispatcher(leaderboardScreenActionDispatcher)
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<InternalAction.GamificationToolbarAction>()?.action },
                    transformMessage = Message::GamificationToolbarMessage
                )
            )
    }
}