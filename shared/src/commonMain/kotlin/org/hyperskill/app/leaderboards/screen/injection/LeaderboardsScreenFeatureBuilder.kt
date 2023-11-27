package org.hyperskill.app.leaderboards.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenActionDispatcher
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenFeature.ViewState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardsScreenReducer
import org.hyperskill.app.leaderboards.screen.view.mapper.LeaderboardsScreenViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object LeaderboardsScreenFeatureBuilder {
    private const val LOG_TAG = "LeaderboardsScreenFeature"

    fun build(
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val leaderboardsScreenReducer = LeaderboardsScreenReducer(
            gamificationToolbarReducer = gamificationToolbarReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val leaderboardsScreenActionDispatcher = LeaderboardsScreenActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val leaderboardsScreenViewStateMapper = LeaderboardsScreenViewStateMapper()

        return ReduxFeature(
            initialState = LeaderboardsScreenFeature.State(
                leaderboardState = LeaderboardsScreenFeature.LeaderboardState.Idle,
                toolbarState = GamificationToolbarFeature.State.Idle
            ),
            reducer = leaderboardsScreenReducer
        )
            .transformState(leaderboardsScreenViewStateMapper::map)
            .wrapWithActionDispatcher(leaderboardsScreenActionDispatcher)
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<InternalAction.GamificationToolbarAction>()?.action },
                    transformMessage = Message::GamificationToolbarMessage
                )
            )
    }
}