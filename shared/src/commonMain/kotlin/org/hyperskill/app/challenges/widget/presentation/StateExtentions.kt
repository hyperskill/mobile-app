package org.hyperskill.app.challenges.widget.presentation

import org.hyperskill.app.challenges.domain.model.Challenge

internal fun ChallengeWidgetFeature.State.getCurrentChallenge(): Challenge? =
    if (this is ChallengeWidgetFeature.State.Content) {
        challenges.firstOrNull()
    } else {
        null
    }