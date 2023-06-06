package org.hyperskill.app.profile.domain.interactor

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class ProfileInteractor(
    private val profileRepository: ProfileRepository,
    submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRepository.selectTrackWithProject(profileId, trackId, projectId)

    suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        profileRepository.selectTrack(profileId, trackId)
}