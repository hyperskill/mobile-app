package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge

// TODO: How to determine the current challenge?
internal fun ChallengeWidgetFeature.State.getCurrentChallenge(): Challenge? =
    when (this) {
        is ChallengeWidgetFeature.State.Content -> challenges.firstOrNull()
        else -> null
    }