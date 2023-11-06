package org.hyperskill.app.share_streak.injection

import org.hyperskill.app.share_streak.domain.interactor.ShareStreakInteractor

interface ShareStreakDataComponent {
    val shareStreakInteractor: ShareStreakInteractor
}