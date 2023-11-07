package org.hyperskill.app.share_streak.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.share_streak.cache.ShareStreakCacheDataSourceImpl
import org.hyperskill.app.share_streak.data.repository.ShareStreakRepositoryImpl
import org.hyperskill.app.share_streak.data.source.ShareStreakCacheDataSource
import org.hyperskill.app.share_streak.domain.interactor.ShareStreakInteractor
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository

internal class ShareStreakDataComponentImpl(appGraph: AppGraph) : ShareStreakDataComponent {
    private val shareStreakCacheDataSource: ShareStreakCacheDataSource =
        ShareStreakCacheDataSourceImpl(appGraph.commonComponent.settings)

    override val shareStreakRepository: ShareStreakRepository
        get() = ShareStreakRepositoryImpl(shareStreakCacheDataSource)

    override val shareStreakInteractor: ShareStreakInteractor
        get() = ShareStreakInteractor(shareStreakRepository)
}