package org.hyperskill.app.profile.domain.interactor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository
import org.hyperskill.app.streaks.domain.model.Streak

class ProfileInteractor(
    private val profileRepository: ProfileRepository,
    private val hypercoinsBalanceMutableSharedFlow: MutableSharedFlow<Int>,
    private val streakMutableSharedFlow: MutableSharedFlow<Streak?>,
    submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    suspend fun getCurrentProfile(sourceType: DataSourceType = DataSourceType.CACHE): Result<Profile> =
        profileRepository.getCurrentProfile(sourceType)

    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRepository.selectTrackWithProject(profileId, trackId, projectId)

    suspend fun clearCache() {
        profileRepository.clearCache()
    }

    fun observeHypercoinsBalance(): SharedFlow<Int> =
        hypercoinsBalanceMutableSharedFlow

    suspend fun notifyHypercoinsBalanceChanged(hypercoinsBalance: Int) {
        hypercoinsBalanceMutableSharedFlow.emit(hypercoinsBalance)
    }

    fun observeStreak(): SharedFlow<Streak?> =
        streakMutableSharedFlow

    suspend fun notifyStreakChanged(streak: Streak?) {
        streakMutableSharedFlow.emit(streak)
    }
}