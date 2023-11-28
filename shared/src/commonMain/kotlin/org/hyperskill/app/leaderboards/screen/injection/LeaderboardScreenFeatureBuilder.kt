package org.hyperskill.app.leaderboards.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenActionDispatcher
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.Message
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenFeature.ViewState
import org.hyperskill.app.leaderboards.screen.presentation.LeaderboardScreenReducer
import org.hyperskill.app.leaderboards.screen.view.mapper.LeaderboardScreenViewStateMapper
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetActionDispatcher
import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetReducer
import org.hyperskill.app.leaderboards.widget.view.mapper.LeaderboardWidgetViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object LeaderboardScreenFeatureBuilder {
    private const val LOG_TAG = "LeaderboardScreenFeature"

    fun build(
        leaderWidgetReducer: LeaderboardWidgetReducer,
        leaderboardWidgetActionDispatcher: LeaderboardWidgetActionDispatcher,
        leaderboardWidgetViewStateMapper: LeaderboardWidgetViewStateMapper,
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val leaderboardScreenReducer = LeaderboardScreenReducer(
            leaderWidgetReducer = leaderWidgetReducer,
            gamificationToolbarReducer = gamificationToolbarReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val leaderboardScreenActionDispatcher = LeaderboardScreenActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor
        )

        val leaderboardScreenViewStateMapper = LeaderboardScreenViewStateMapper(
            leaderboardWidgetViewStateMapper = leaderboardWidgetViewStateMapper
        )

        return ReduxFeature(
            initialState = LeaderboardScreenFeature.initialState(),
            reducer = leaderboardScreenReducer
        )
            .transformState(leaderboardScreenViewStateMapper::map)
            .wrapWithActionDispatcher(leaderboardScreenActionDispatcher)
            .wrapWithActionDispatcher(
                leaderboardWidgetActionDispatcher.transform(
                    transformAction = { it.safeCast<InternalAction.LeaderboardWidgetAction>()?.action },
                    transformMessage = Message::LeaderboardWidgetMessage
                )
            )
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<InternalAction.GamificationToolbarAction>()?.action },
                    transformMessage = Message::GamificationToolbarMessage
                )
            )
    }
}