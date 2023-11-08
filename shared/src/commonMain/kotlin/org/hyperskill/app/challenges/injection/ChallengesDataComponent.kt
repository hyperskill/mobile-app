package org.hyperskill.app.challenges.injection

import org.hyperskill.app.challenges.domain.repository.ChallengesRepository

interface ChallengesDataComponent {
    val challengesRepository: ChallengesRepository
}