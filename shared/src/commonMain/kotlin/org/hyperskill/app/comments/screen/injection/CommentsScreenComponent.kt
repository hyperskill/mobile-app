package org.hyperskill.app.comments.screen.injection

import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface CommentsScreenComponent {
    val commentThreadTitleMapper: CommentThreadTitleMapper
    val commentsScreenFeature: Feature<CommentsScreenViewState, Message, Action>
}