package org.hyperskill.app.profile.domain.interactor

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class ProfileInteractor(
    private val profileRepository: ProfileRepository,
    submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow

    suspend fun getCurrentProfile(sourceType: DataSourceType = DataSourceType.CACHE): Result<Profile> =
        profileRepository.getCurrentProfile(sourceType)

    suspend fun clearCache() {
        profileRepository.clearCache()
    }
}