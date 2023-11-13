package org.hyperskill.app.challenges.widget.injection

import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions

internal class ChallengeWidgetComponentImpl(private val appGraph: AppGraph) : ChallengeWidgetComponent {
    override val challengeWidgetReducer: ChallengeWidgetReducer
        get() = ChallengeWidgetReducer()

    override val challengeWidgetActionDispatcher: ChallengeWidgetActionDispatcher
        get() = ChallengeWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            challengesRepository = appGraph.buildChallengesDataComponent().challengesRepository
        )

    override val challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper
        get() = ChallengeWidgetViewStateMapper()
}