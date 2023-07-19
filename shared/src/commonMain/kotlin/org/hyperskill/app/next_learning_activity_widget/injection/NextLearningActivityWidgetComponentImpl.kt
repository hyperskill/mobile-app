package org.hyperskill.app.next_learning_activity_widget.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetActionDispatcher
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetReducer

internal class NextLearningActivityWidgetComponentImpl(
    private val appGraph: AppGraph
) : NextLearningActivityWidgetComponent {
    override val nextLearningActivityWidgetReducer: NextLearningActivityWidgetReducer
        get() = NextLearningActivityWidgetReducer()

    override val nextLearningActivityWidgetActionDispatcher: NextLearningActivityWidgetActionDispatcher
        get() = NextLearningActivityWidgetActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.stateRepositoriesComponent.nextLearningActivityStateRepository,
            appGraph.stateRepositoriesComponent.currentStudyPlanStateRepository,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}