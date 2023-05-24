package org.hyperskill.app.project_selection_details.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection_details.domain.interactor.ProjectSelectionDetailsInteractor
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsActionDispatcher
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.ViewState
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsReducer
import org.hyperskill.app.project_selection_details.view.ProjectSelectionDetailsViewStateMapper
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.providers.domain.repository.ProvidersRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProjectSelectionDetailsFeatureBuilder {
    fun build(
        params: ProjectSelectionDetailsParams,
        trackRepository: TrackRepository,
        projectsRepository: ProjectsRepository,
        progressesRepository: ProgressesRepository,
        providersRepository: ProvidersRepository,
        profileRepository: ProfileRepository,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        numbersFormatter: NumbersFormatter,
        dateFormatter: SharedDateFormatter
    ): Feature<ViewState, Message, Action> {
        val projectSelectionDetailsReducer = ProjectSelectionDetailsReducer()

        val projectSelectionDetailsInteractor = ProjectSelectionDetailsInteractor(
            trackRepository = trackRepository,
            projectsRepository = projectsRepository,
            progressesRepository = progressesRepository,
            providersRepository = providersRepository,
            profileRepository = profileRepository
        )
        val projectSelectionDetailsActionDispatcher = ProjectSelectionDetailsActionDispatcher(
            config = ActionDispatcherOptions(),
            projectSelectionDetailsInteractor = projectSelectionDetailsInteractor,
            sentryInteractor = sentryInteractor,
            analyticInteractor = analyticInteractor
        )

        val projectSelectionDetailsViewStateMapper = ProjectSelectionDetailsViewStateMapper(
            resourceProvider = resourceProvider,
            numbersFormatter = numbersFormatter,
            dateFormatter = dateFormatter
        )

        val projectSelectionDetailsFeatureInitialState = ProjectSelectionDetailsFeature.initialState(
            trackId = params.trackId,
            projectId = params.projectId,
            isProjectSelected = params.isProjectSelected
        )

        return ReduxFeature(
            projectSelectionDetailsFeatureInitialState,
            projectSelectionDetailsReducer
        )
            .wrapWithActionDispatcher(projectSelectionDetailsActionDispatcher)
            .transformState(projectSelectionDetailsViewStateMapper::map)
    }
}