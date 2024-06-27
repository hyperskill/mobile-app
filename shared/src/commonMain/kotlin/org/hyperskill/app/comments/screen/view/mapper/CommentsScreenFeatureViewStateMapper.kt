package org.hyperskill.app.comments.screen.view.mapper

import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.core.view.mapper.ResourceProvider

internal class CommentsScreenFeatureViewStateMapper(
    private val commentThreadTitleMapper: CommentThreadTitleMapper,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: CommentsScreenFeature.State): CommentsScreenFeature.ViewState =
        CommentsScreenFeature.ViewState(
            navigationTitle = commentThreadTitleMapper.getFormattedCommentThreadStatistics(
                thread = state.commentStatistics.thread,
                count = state.commentStatistics.totalCount
            )
        )
}