package org.hyperskill.app.topic_completed_modal.injection

import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface TopicCompletedModalComponent {
    val topicCompletedModalFeature: Feature<ViewState, Message, Action>
}