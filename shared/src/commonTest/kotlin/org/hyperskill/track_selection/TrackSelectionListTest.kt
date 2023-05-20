package org.hyperskill.track_selection

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.State
import org.hyperskill.app.track_selection.presentation.TrackSelectionListReducer
import org.hyperskill.app.track_selection.view.TrackSelectionListViewStateMapper

class TrackSelectionListTest {

    private val trackSelectionListReducer = TrackSelectionListReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = TrackSelectionListViewStateMapper(
        numbersFormatter = NumbersFormatter(resourceProvider),
        dateFormatter = SharedDateFormatter(resourceProvider)
    )

    @Test
    fun `Initialize message should trigger content loading`() {
        val (state, actions) = trackSelectionListReducer.reduce(
            State.Idle,
            Message.Initialize
        )
        assertContains(
            actions,
            InternalAction.FetchTracks
        )
        assertEquals(
            State.Loading,
            state
        )
    }

    @Test
    fun `FetchContent success message should update content`() {
        val selectedTrackId = 3L
        val tracks = listOf(0L, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)

        val (state, actions) = trackSelectionListReducer.reduce(
            State.Loading,
            TrackSelectionListFeature.TracksFetchResult.Success(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            )
        )

        assertTrue(actions.isEmpty())

        val expectedContent = State.Content(
            tracks = tracks,
            selectedTrackId = selectedTrackId
        )
        assertEquals(
            expectedContent,
            state
        )
    }

    @Test
    fun `Selected track should be first in the tracks list`() {
        val selectedTrackId = 3L
        val tracks = listOf(0L, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)

        val state = State.Content(
            tracks = tracks,
            selectedTrackId = selectedTrackId
        )

        val viewState = viewStateMapper.map(state)

        assertTrue(viewState is TrackSelectionListFeature.ViewState.Content)

        assertTrue {
            val firstTrack = viewState.tracks.first()
            firstTrack.isSelected && firstTrack.id == selectedTrackId
        }
    }

    @Test
    fun `RetryContentLoading message should trigger force content loading`() {
        val (state, actions) = trackSelectionListReducer.reduce(
            State.NetworkError,
            Message.RetryContentLoading
        )
        assertContains(
            actions,
            InternalAction.FetchTracks
        )
        assertEquals(
            State.Loading,
            state
        )
    }

    @Test
    fun `TrackClicked message should trigger modal confirmation`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val tracks = listOf(trackId, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            Message.TrackClicked(trackId)
        )
        assertContains(
            actions,
            Action.ViewAction.ShowTrackSelectionConfirmationModal(track = tracks[0].track)
        )
    }

    @Test
    fun `Confirming track selection should trigger action`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val tracks = listOf(trackId, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            Message.TrackSelectionConfirmationResult(trackId, isConfirmed = true)
        )
        assertContains(
            actions,
            InternalAction.SelectTrack(trackId = trackId)
        )
    }

    @Test
    fun `Canceling track selection should not trigger action`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val tracks = listOf(trackId, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            Message.TrackSelectionConfirmationResult(trackId, isConfirmed = false)
        )
        assertFalse {
            actions.any { it is InternalAction.SelectTrack }
        }
    }

    @Test
    fun `Successful track selection should trigger success status and navigation to the StudyPlan screen`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val tracks = listOf(trackId, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            TrackSelectionListFeature.TrackSelectionResult.Success
        )
        assertContains(
            actions,
            Action.ViewAction.ShowTrackSelectionStatus.Success
        )
        assertContains(
            actions,
            Action.ViewAction.NavigateTo.StudyPlan
        )
    }

    @Test
    fun `Failed track selection should trigger error status and don't navigate to the StudyPlan screen`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val tracks = listOf(trackId, 1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            TrackSelectionListFeature.TrackSelectionResult.Error
        )
        assertContains(
            actions,
            Action.ViewAction.ShowTrackSelectionStatus.Error
        )
        assertTrue {
            actions.none { it is Action.ViewAction.NavigateTo.StudyPlan }
        }
    }
}