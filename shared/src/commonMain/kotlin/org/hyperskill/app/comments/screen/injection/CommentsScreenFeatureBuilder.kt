package org.hyperskill.app.comments.screen.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.comments.screen.domain.interactor.CommentsScreenInteractor
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.comments.screen.presentation.CommentsScreenActionDispatcher
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.InternalAction
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.presentation.CommentsScreenReducer
import org.hyperskill.app.comments.screen.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.comments.screen.view.mapper.CommentsScreenViewStateMapper
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object CommentsScreenFeatureBuilder {
    private const val LOG_TAG = "CommentsScreenFeature"

    fun build(
        params: CommentsScreenFeatureParams,
        commentsScreenInteractor: CommentsScreenInteractor,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor,
        commentThreadTitleMapper: CommentThreadTitleMapper,
        dateFormatter: SharedDateFormatter,
        buildVariant: BuildVariant,
        logger: Logger
    ): Feature<CommentsScreenViewState, Message, Action> {
        val commentsScreenReducer = CommentsScreenReducer()
            .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val commentsScreenActionDispatcher = CommentsScreenActionDispatcher(
            config = ActionDispatcherOptions(),
            commentsScreenInteractor = commentsScreenInteractor,
            sentryInteractor = sentryInteractor
        )

        val commentsScreenViewStateMapper = CommentsScreenViewStateMapper(
            commentThreadTitleMapper = commentThreadTitleMapper,
            dateFormatter = dateFormatter
        )

        return ReduxFeature(
            initialState = CommentsScreenFeature.initialState(params),
            reducer = commentsScreenReducer
        )
            .wrapWithActionDispatcher(commentsScreenActionDispatcher)
            .transformState(commentsScreenViewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}