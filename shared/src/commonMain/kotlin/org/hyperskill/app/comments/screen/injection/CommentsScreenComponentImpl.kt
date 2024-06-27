package org.hyperskill.app.comments.screen.injection

import org.hyperskill.app.comments.screen.domain.interactor.CommentsScreenInteractor
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.ViewState
import org.hyperskill.app.core.injection.AppGraph
import ru.nobird.app.presentation.redux.feature.Feature

internal class CommentsScreenComponentImpl(
    private val appGraph: AppGraph,
    private val params: CommentsScreenFeatureParams
) : CommentsScreenComponent {
    private val commentsScreenInteractor: CommentsScreenInteractor =
        CommentsScreenInteractor(
            discussionsRepository = appGraph.buildDiscussionsDataComponent().discussionsRepository,
            commentsRepository = appGraph.buildCommentsDataComponent().commentsRepository
        )

    override val commentsScreenFeature: Feature<ViewState, Message, Action>
        get() = CommentsScreenFeatureBuilder.build(
            params = params,
            commentsScreenInteractor = commentsScreenInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            logger = appGraph.loggerComponent.logger
        )
}