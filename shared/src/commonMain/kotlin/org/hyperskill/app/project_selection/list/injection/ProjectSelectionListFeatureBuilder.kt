package org.hyperskill.app.project_selection.list.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListActionDispatcher
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListReducer
import org.hyperskill.app.project_selection.list.view.mapper.ProjectSelectionListViewStateMapper
import org.hyperskill.app.projects.domain.repository.ProjectsRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.track.domain.repository.TrackRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProjectSelectionListFeatureBuilder {
    private const val LOG_TAG = "ProjectSelectionListFeature"

    fun build(
        params: ProjectSelectionListParams,
        trackRepository: TrackRepository,
        currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
        projectsRepository: ProjectsRepository,
        progressesRepository: ProgressesRepository,
        currentProfileStateRepository: CurrentProfileStateRepository,
        viewStateMapper: ProjectSelectionListViewStateMapper,
        sentryInteractor: SentryInteractor,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val actionDispatcher = ProjectSelectionListActionDispatcher(
            config = ActionDispatcherOptions(),
            trackRepository = trackRepository,
            currentStudyPlanStateRepository = currentStudyPlanStateRepository,
            projectsRepository = projectsRepository,
            progressesRepository = progressesRepository,
            currentProfileStateRepository = currentProfileStateRepository,
            sentryInteractor = sentryInteractor,
            analyticInteractor = analyticInteractor
        )
        return ReduxFeature(
            initialState = ProjectSelectionListFeature.initialState(
                trackId = params.trackId,
                isNewUserMode = params.isNewUserMode
            ),
            reducer = ProjectSelectionListReducer().wrapWithLogger(buildVariant, logger, LOG_TAG)
        )
            .wrapWithActionDispatcher(actionDispatcher)
            .transformState {
                viewStateMapper.map(it.content)
            }
    }
}