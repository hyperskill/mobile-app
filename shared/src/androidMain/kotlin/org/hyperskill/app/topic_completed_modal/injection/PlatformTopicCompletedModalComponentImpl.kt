package org.hyperskill.app.topic_completed_modal.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalViewModel

internal class PlatformTopicCompletedModalComponentImpl(
    private val topicCompletedModalComponent: TopicCompletedModalComponent
) : PlatformTopicCompletedModalComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                TopicCompletedModalViewModel::class.java to {
                    TopicCompletedModalViewModel(
                        viewContainer = topicCompletedModalComponent.topicCompletedModalFeature.wrapWithFlowView()
                    )
                }
            )
        )
}