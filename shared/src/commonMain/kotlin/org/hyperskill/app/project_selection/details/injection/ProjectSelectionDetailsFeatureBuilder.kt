package org.hyperskill.app.project_selection.details.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.details.domain.interactor.ProjectSelectionDetailsInteractor
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsActionDispatcher
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsReducer
import org.hyperskill.app.project_selection.details.view.ProjectSelectionDetailsViewStateMapper
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProjectSelectionDetailsFeatureBuilder {
    private const val LOG_TAG = "ProjectSelectionDetailsFeature"

    fun build(
        params: ProjectSelectionDetailsParams,
        trackRepository: TrackRepository,
        projectsRepository: ProjectsRepository,
        progressesRepository: ProgressesRepository,
        providersRepository: ProvidersRepository,
        profileRepository: ProfileRepository,
        currentProfileStateRepository: CurrentProfileStateRepository,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        numbersFormatter: NumbersFormatter,
        dateFormatter: SharedDateFormatter,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val projectSelectionDetailsReducer =
            ProjectSelectionDetailsReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)

        val projectSelectionDetailsInteractor = ProjectSelectionDetailsInteractor(
            trackRepository = trackRepository,
            projectsRepository = projectsRepository,
            progressesRepository = progressesRepository,
            providersRepository = providersRepository,
            profileRepository = profileRepository,
            currentProfileStateRepository = currentProfileStateRepository
        )
        val projectSelectionDetailsActionDispatcher = ProjectSelectionDetailsActionDispatcher(
            config = ActionDispatcherOptions(),
            projectSelectionDetailsInteractor = projectSelectionDetailsInteractor,
            sentryInteractor = sentryInteractor
        )

        val projectSelectionDetailsViewStateMapper = ProjectSelectionDetailsViewStateMapper(
            resourceProvider = resourceProvider,
            numbersFormatter = numbersFormatter,
            dateFormatter = dateFormatter
        )

        val projectSelectionDetailsFeatureInitialState = ProjectSelectionDetailsFeature.initialState(
            isNewUserMode = params.isNewUserMode,
            trackId = params.trackId,
            projectId = params.projectId,
            isProjectSelected = params.isProjectSelected,
            isProjectBestRated = params.isProjectBestRated,
            isProjectFastestToComplete = params.isProjectFastestToComplete
        )

        return ReduxFeature(
            projectSelectionDetailsFeatureInitialState,
            projectSelectionDetailsReducer
        )
            .wrapWithActionDispatcher(projectSelectionDetailsActionDispatcher)
            .transformState(projectSelectionDetailsViewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? ProjectSelectionDetailsFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}