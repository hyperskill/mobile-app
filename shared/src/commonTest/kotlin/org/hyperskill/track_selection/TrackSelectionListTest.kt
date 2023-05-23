package org.hyperskill.track_selection

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.State
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListReducer
import org.hyperskill.app.track_selection.list.view.TrackSelectionListViewStateMapper

class TrackSelectionListTest {

    private val trackSelectionListReducer = TrackSelectionListReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = TrackSelectionListViewStateMapper(
        resourceProvider = resourceProvider,
        numbersFormatter = NumbersFormatter(resourceProvider)
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
    fun `TrackClicked message should trigger navigation to details screen`() {
        val trackId = 0L
        val selectedTrackId = 3L
        val trackWithProgress = TrackWithProgress.Companion.stub(trackId)
        val tracks = listOf(1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub) + trackWithProgress
        val (_, actions) = trackSelectionListReducer.reduce(
            State.Content(
                tracks = tracks,
                selectedTrackId = selectedTrackId
            ),
            Message.TrackClicked(trackId)
        )
        assertContains(
            actions,
            Action.ViewAction.NavigateTo.TrackDetails(trackWithProgress, false)
        )
    }
}