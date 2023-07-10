package org.hyperskill.progress_screen

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.progress_screen.domain.analytic.ProgressScreenClickedChangeProjectHyperskillAnalyticEvent
import org.hyperskill.app.progress_screen.domain.analytic.ProgressScreenClickedChangeTrackHyperskillAnalyticEvent
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature
import org.hyperskill.app.progress_screen.presentation.ProgressScreenReducer
import org.hyperskill.app.progress_screen.view.ProgressScreenViewStateMapper
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.profile.stub
import org.hyperskill.projects_selection.stub
import org.hyperskill.study_plan.domain.model.stub
import org.hyperskill.track.stub
import org.hyperskill.track_selection.stub

class ProgressScreenTest {
    private val progressScreenReducer = ProgressScreenReducer()

    private val viewStateMapper = ProgressScreenViewStateMapper(
        SharedDateFormatter(ResourceProviderStub()),
        ResourceProviderStub()
    )

    @Test
    fun `Empty fetch project with progress result should reduce project progress state to idle`() {
        val (actualState, _) = progressScreenReducer.reduce(
            ProgressScreenFeature.State(
                trackProgressState = ProgressScreenFeature.TrackProgressState.Loading,
                projectProgressState = ProgressScreenFeature.ProjectProgressState.Loading,
                isTrackProgressRefreshing = false,
                isProjectProgressRefreshing = false
            ),
            ProgressScreenFeature.ProjectWithProgressFetchResult.Empty
        )

        assertEquals(ProgressScreenFeature.ProjectProgressState.Empty, actualState.projectProgressState)
    }

    @Test
    fun `Loading of both track and progress after pull to refresh should change refreshing flag to false`() {
        val (refreshingState, _) = progressScreenReducer.reduce(
            ProgressScreenFeature.State(
                trackProgressState = ProgressScreenFeature.TrackProgressState.Loading,
                projectProgressState = ProgressScreenFeature.ProjectProgressState.Loading,
                isTrackProgressRefreshing = true,
                isProjectProgressRefreshing = true
            ),
            ProgressScreenFeature.TrackWithProgressFetchResult.Success(
                TrackWithProgress(Track.stub(1), TrackProgress.stub(1)),
                StudyPlan.stub(),
                Profile.stub()
            )
        )

        assertEquals(true, viewStateMapper.map(refreshingState).isRefreshing)

        val (loadedStateSuccess, _) = progressScreenReducer.reduce(
            refreshingState,
            ProgressScreenFeature.ProjectWithProgressFetchResult.Success(
                ProjectWithProgress(Project.stub(1), ProjectProgress.stub(1)),
                StudyPlan.stub()
            )
        )

        assertEquals(false, viewStateMapper.map(loadedStateSuccess).isRefreshing)

        val (loadedStateEmpty, _) = progressScreenReducer.reduce(
            refreshingState,
            ProgressScreenFeature.ProjectWithProgressFetchResult.Empty
        )

        assertEquals(false, viewStateMapper.map(loadedStateEmpty).isRefreshing)
    }

    @Test
    fun `If both track and project progress are in error state then isInErrorState returns true`() {
        val state = ProgressScreenFeature.State(
            trackProgressState = ProgressScreenFeature.TrackProgressState.Error,
            projectProgressState = ProgressScreenFeature.ProjectProgressState.Error,
            isTrackProgressRefreshing = false,
            isProjectProgressRefreshing = false
        )
        val viewState = viewStateMapper.map(state)

        assertEquals(true, viewState.isInErrorState)
    }

    @Test
    fun `If both track and project progress are in error state then RetryContentLoading message retries loading`() {
        val initialState = ProgressScreenFeature.State(
            trackProgressState = ProgressScreenFeature.TrackProgressState.Error,
            projectProgressState = ProgressScreenFeature.ProjectProgressState.Error,
            isTrackProgressRefreshing = false,
            isProjectProgressRefreshing = false
        )

        val (actualState, actualActions) = progressScreenReducer.reduce(
            initialState,
            ProgressScreenFeature.Message.RetryContentLoading
        )

        val expectedState = initialState.copy(
            trackProgressState = ProgressScreenFeature.TrackProgressState.Loading,
            projectProgressState = ProgressScreenFeature.ProjectProgressState.Loading
        )
        val expectedActions: Set<ProgressScreenFeature.Action> = setOf(
            ProgressScreenFeature.InternalAction.FetchTrackWithProgress(forceLoadFromNetwork = true),
            ProgressScreenFeature.InternalAction.FetchProjectWithProgress(forceLoadFromNetwork = true)
        )

        assertEquals(expectedState, actualState)
        assertEquals(expectedActions, actualActions)
    }

    @Test
    fun `Click on change track button triggers logging analytic event and navigation to track selection screen`() {
        val studyPlan = StudyPlan.stub()
        val initialState = ProgressScreenFeature.State(
            trackProgressState = ProgressScreenFeature.TrackProgressState.Content(
                trackWithProgress = TrackWithProgress.stub(),
                studyPlan = studyPlan,
                profile = Profile.stub()
            ),
            projectProgressState = ProgressScreenFeature.ProjectProgressState.Content(
                projectWithProgress = ProjectWithProgress.stub(),
                studyPlan = studyPlan
            ),
            isTrackProgressRefreshing = false,
            isProjectProgressRefreshing = false
        )

        val (_, actions) = progressScreenReducer.reduce(
            initialState,
            ProgressScreenFeature.Message.ChangeTrackButtonClicked
        )

        assertContains(actions, ProgressScreenFeature.Action.ViewAction.NavigateTo.TrackSelectionScreen)
        assertTrue {
            actions.any {
                it is ProgressScreenFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProgressScreenClickedChangeTrackHyperskillAnalyticEvent
            }
        }
    }

    @Test
    fun `Click on change project button triggers logging analytic event and navigation to project selection screen`() {
        val trackId = 1L
        val studyPlan = StudyPlan.stub()
        val initialState = ProgressScreenFeature.State(
            trackProgressState = ProgressScreenFeature.TrackProgressState.Content(
                trackWithProgress = TrackWithProgress.stub(trackId = trackId),
                studyPlan = studyPlan,
                profile = Profile.stub()
            ),
            projectProgressState = ProgressScreenFeature.ProjectProgressState.Content(
                projectWithProgress = ProjectWithProgress.stub(),
                studyPlan = studyPlan
            ),
            isTrackProgressRefreshing = false,
            isProjectProgressRefreshing = false
        )

        val (_, actions) = progressScreenReducer.reduce(
            initialState,
            ProgressScreenFeature.Message.ChangeProjectButtonClicked
        )

        assertContains(actions, ProgressScreenFeature.Action.ViewAction.NavigateTo.ProjectSelectionScreen(trackId))
        assertTrue {
            actions.any {
                it is ProgressScreenFeature.InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is ProgressScreenClickedChangeProjectHyperskillAnalyticEvent
            }
        }
    }
}