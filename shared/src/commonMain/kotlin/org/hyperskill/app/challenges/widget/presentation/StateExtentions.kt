package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge
import ru.nobird.app.core.model.mutate

internal fun ChallengeWidgetFeature.State.Content.setCurrentChallengeIntervalProgressAsCompleted(): Challenge? {
    val currentChallenge = challenge ?: return null

    val currentIntervalIndex = currentChallenge.currentInterval
        ?.minus(1)
        ?.takeIf { it > 0 && currentChallenge.progress.size > it }
        ?: return null

    val newProgress = currentChallenge.progress.mutate {
        set(
            index = currentIntervalIndex,
            element = true
        )
    }

    return currentChallenge.copy(progress = newProgress)
}