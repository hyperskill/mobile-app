package org.hyperskill.app.main.domain.interactor

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

class AppInteractor(
    private val authInteractor: AuthInteractor,
    private val currentProfileRepository: CurrentProfileStateRepository,
    private val userStorageInteractor: UserStorageInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val progressesRepository: ProgressesRepository,
    private val trackRepository: TrackRepository,
    private val providersRepository: ProvidersRepository,
    private val projectsRepository: ProjectsRepository
) {
    suspend fun doCurrentUserSignedOutCleanUp() {
        analyticInteractor.flushEvents()
        clearCache()
    }

    // TODO: define more flexible & scalable way to clear cache
    private suspend fun clearCache() {
        authInteractor.clearCache()
        currentProfileRepository.resetState()
        userStorageInteractor.clearCache()
        progressesRepository.clearCache()
        trackRepository.clearCache()
        providersRepository.clearCache()
        projectsRepository.clearCache()
    }
}