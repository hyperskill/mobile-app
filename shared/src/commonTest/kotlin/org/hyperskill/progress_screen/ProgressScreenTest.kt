package org.hyperskill.progress_screen

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature
import org.hyperskill.app.progresses.presentation.ProgressScreenReducer
import org.hyperskill.app.progresses.view.ProgressScreenViewStateMapper
import org.hyperskill.app.projects.domain.model.Project
import org.hyperskill.app.projects.domain.model.ProjectProgress
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.projects_selection.stub
import org.hyperskill.track.stub
import org.hyperskill.track_selection.stub

class ProgressScreenTest {
    private val progressScreenReducer = ProgressScreenReducer()

    private val viewStateMapper = ProgressScreenViewStateMapper(SharedDateFormatter(ResourceProviderStub()))

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
                TrackWithProgress(Track.stub(1), TrackProgress.stub(1))
            )
        )

        assertEquals(true, viewStateMapper.map(refreshingState).isRefreshing)

        val (loadedStateSuccess, _) = progressScreenReducer.reduce(
            refreshingState,
            ProgressScreenFeature.ProjectWithProgressFetchResult.Success(
                ProjectWithProgress(Project.stub(1), ProjectProgress.stub(1))
            )
        )

        assertEquals(false, viewStateMapper.map(loadedStateSuccess).isRefreshing)

        val (loadedStateEmpty, _) = progressScreenReducer.reduce(
            refreshingState,
            ProgressScreenFeature.ProjectWithProgressFetchResult.Empty
        )

        assertEquals(false, viewStateMapper.map(loadedStateEmpty).isRefreshing)
    }
}