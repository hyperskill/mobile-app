package org.hyperskill.app.profile.domain.interactor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class ProfileInteractor(
    private val profileRepository: ProfileRepository,
    private val hypercoinsBalanceMutableSharedFlow: MutableSharedFlow<Int>,
    submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    suspend fun getCurrentProfile(forceLoadFromNetwork: Boolean = false): Result<Profile> =
        profileRepository.getState(forceUpdate = forceLoadFromNetwork)

    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRepository.selectTrackWithProject(profileId, trackId, projectId)

    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        profileRepository.selectTrack(profileId, trackId)

    suspend fun clearCache() {
        profileRepository.resetState()
    }

    fun observeHypercoinsBalance(): SharedFlow<Int> =
        hypercoinsBalanceMutableSharedFlow

    suspend fun notifyHypercoinsBalanceChanged(hypercoinsBalance: Int) {
        hypercoinsBalanceMutableSharedFlow.emit(hypercoinsBalance)
    }
}