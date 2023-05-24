package org.hyperskill.app.project_selection.details.domain.interactor

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.track.domain.repository.TrackRepository

internal class ProjectSelectionDetailsInteractor(
    private val trackRepository: TrackRepository,
    private val projectsRepository: ProjectsRepository,
    private val progressesRepository: ProgressesRepository,
    private val providersRepository: ProvidersRepository,
    private val profileRepository: ProfileRepository
) {
    suspend fun getContentData(
        trackId: Long,
        projectId: Long,
        forceLoadFromNetwork: Boolean
    ): Result<ProjectSelectionDetailsFeature.ContentData> =
        coroutineScope {
            kotlin.runCatching {
                val trackDeferred = async { trackRepository.getTrack(trackId, forceLoadFromNetwork) }
                val projectDeferred = async { projectsRepository.getProject(projectId, forceLoadFromNetwork) }

                val project = projectDeferred.await().getOrThrow()

                val projectProgressDeferred = async {
                    progressesRepository.getProjectProgress(projectId, forceLoadFromNetwork)
                }
                val providerDeferred = async {
                    if (project.providerId != null) {
                        providersRepository.getProvider(project.providerId, forceLoadFromNetwork)
                    } else {
                        null
                    }
                }

                val track = trackDeferred.await().getOrThrow()

                val projectProgress = projectProgressDeferred.await().getOrThrow()
                val provider = providerDeferred.await()?.getOrThrow()

                ProjectSelectionDetailsFeature.ContentData(
                    track = track,
                    project = ProjectWithProgress(
                        project = project,
                        progress = projectProgress
                    ),
                    provider = provider
                )
            }
        }

    suspend fun selectProject(trackId: Long, projectId: Long): Result<Profile> =
        kotlin.runCatching {
            val currentProfile = profileRepository
                .getCurrentProfile(DataSourceType.CACHE)
                .getOrThrow()

            profileRepository
                .selectTrackWithProject(
                    profileId = currentProfile.id,
                    trackId = trackId,
                    projectId = projectId
                )
                .getOrThrow()
        }

    fun clearProjectsCache() {
        projectsRepository.clearCache()
    }
}