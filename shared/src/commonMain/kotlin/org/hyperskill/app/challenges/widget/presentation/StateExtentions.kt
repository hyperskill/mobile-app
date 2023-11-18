package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge
import ru.nobird.app.core.model.mutate

internal fun ChallengeWidgetFeature.State.getCurrentChallenge(): Challenge? =
    if (this is ChallengeWidgetFeature.State.Content) {
        challenges.firstOrNull()
    } else {
        null
    }

internal fun ChallengeWidgetFeature.State.setCurrentChallengeIntervalProgressAsCompleted(): List<Challenge>? {
    if (this !is ChallengeWidgetFeature.State.Content) {
        return null
    }

    val currentChallenge = getCurrentChallenge() ?: return null
    val currentChallengeIndex = challenges
        .indexOfFirst { it.id == currentChallenge.id }
        .takeIf { it != -1 } ?: return null

    val currentIntervalIndex = currentChallenge.currentInterval?.minus(1) ?: return null
    val newProgress = currentChallenge.progress.mapIndexed { index, progress ->
        if (index == currentIntervalIndex) {
            true
        } else {
            progress
        }
    }

    return challenges.mutate {
        set(
            index = currentChallengeIndex,
            element = currentChallenge.copy(progress = newProgress)
        )
    }
}