package org.hyperskill.app.topic_completed_modal.view

import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.State
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState

internal class TopicCompletedModalViewStateMapper {

    fun map(state: State): ViewState =
        ViewState(
            title = "",
            description = ""
        )
}