package org.hyperskill.app.comments.screen.injection

import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Action
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.Message
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface CommentsScreenComponent {
    val commentsScreenFeature: Feature<ViewState, Message, Action>
}