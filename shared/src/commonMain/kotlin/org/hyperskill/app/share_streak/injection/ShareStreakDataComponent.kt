package org.hyperskill.app.share_streak.injection

import org.hyperskill.app.share_streak.domain.interactor.ShareStreakInteractor
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository

interface ShareStreakDataComponent {
    val shareStreakRepository: ShareStreakRepository
    val shareStreakInteractor: ShareStreakInteractor
}