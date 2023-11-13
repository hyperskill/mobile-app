package org.hyperskill.app.challenges.widget.view.mapper

import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.presentation.getCurrentChallenge
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState

class ChallengeWidgetViewStateMapper {
    fun map(state: ChallengeWidgetFeature.State): ChallengeWidgetViewState =
        when (state) {
            ChallengeWidgetFeature.State.Idle -> ChallengeWidgetViewState.Idle
            ChallengeWidgetFeature.State.Loading -> ChallengeWidgetViewState.Loading
            ChallengeWidgetFeature.State.Error -> ChallengeWidgetViewState.Error
            is ChallengeWidgetFeature.State.Content -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: ChallengeWidgetFeature.State.Content): ChallengeWidgetViewState {
        val currentChallenge = state.getCurrentChallenge() ?: return ChallengeWidgetViewState.Empty

        return ChallengeWidgetViewState.Content(
            challenge = currentChallenge
        )
    }
}