package org.hyperskill.app.challenges.widget.view.model

import org.hyperskill.app.challenges.domain.model.Challenge

sealed interface ChallengeWidgetViewState {
    object Idle : ChallengeWidgetViewState
    object Loading : ChallengeWidgetViewState
    object Error : ChallengeWidgetViewState
    object Empty : ChallengeWidgetViewState
    data class Content(
        val challenge: Challenge
    ) : ChallengeWidgetViewState
}