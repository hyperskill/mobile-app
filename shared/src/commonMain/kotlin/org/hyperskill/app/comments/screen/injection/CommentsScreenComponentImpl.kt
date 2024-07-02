package org.hyperskill.app.comments.screen.injection

import org.hyperskill.app.comments.screen.domain.interactor.CommentsScreenInteractor
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.core.injection.AppGraph
import ru.nobird.app.presentation.redux.feature.Feature

internal class CommentsScreenComponentImpl(
    private val appGraph: AppGraph,
    private val params: CommentsScreenFeatureParams
) : CommentsScreenComponent {
    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)

    private val commentsScreenInteractor: CommentsScreenInteractor =
        CommentsScreenInteractor(
            discussionsRepository = appGraph.buildDiscussionsDataComponent().discussionsRepository,
            commentsRepository = appGraph.buildCommentsDataComponent().commentsRepository
        )

    override val commentsScreenFeature: Feature<CommentsScreenViewState, Message, Action>
        get() = CommentsScreenFeatureBuilder.build(
            params = params,
            commentsScreenInteractor = commentsScreenInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            commentThreadTitleMapper = commentThreadTitleMapper,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            logger = appGraph.loggerComponent.logger
        )
}