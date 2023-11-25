package org.hyperskill.app.challenges.widget.injection

import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper

interface ChallengeWidgetComponent {
    val challengeWidgetReducer: ChallengeWidgetReducer
    val challengeWidgetActionDispatcher: ChallengeWidgetActionDispatcher
    val challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper
}