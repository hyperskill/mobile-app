package org.hyperskill.track_selection.list

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Action
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.InternalAction
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.Message
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature.State
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListReducer
import org.hyperskill.app.track_selection.list.view.TrackSelectionListViewStateMapper
import org.hyperskill.track_selection.stub

class TrackSelectionListTest {

    private val trackSelectionListReducer = TrackSelectionListReducer(
        params = TrackSelectionListParams(isNewUserMode = false)
    )

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
                selectedTrackId = selectedTrackId,
                tracksSelectionCountMap = emptyMap()
            )
        )

        assertTrue(actions.isEmpty())

        val expectedContent = State.Content(
            tracks = tracks,
            selectedTrackId = selectedTrackId,
            tracksSelectionCountMap = emptyMap()
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
            selectedTrackId = selectedTrackId,
            tracksSelectionCountMap = emptyMap()
        )

        val viewState = viewStateMapper.map(state)

        assertTrue(viewState is TrackSelectionListFeature.ViewState.Content)

        assertTrue {
            val firstTrack = viewState.tracks.first()
            firstTrack.isSelected && firstTrack.id == selectedTrackId
        }
    }

    @Test
    fun `Tracks should be sorted by selection change count`() {
        val selectedTrackId = 3L
        val tracks = listOf(1L, 2L, selectedTrackId, 4L, 5L)
            .map(TrackWithProgress.Companion::stub)
        val tracksSelectionCountMap = mapOf(
            1L to 3,
            2L to 2,
            4L to 5,
            5L to 1
        )
        val state = State.Content(
            tracks = tracks,
            selectedTrackId = selectedTrackId,
            tracksSelectionCountMap = tracksSelectionCountMap
        )

        val viewState = viewStateMapper.map(state) as TrackSelectionListFeature.ViewState.Content

        assertEquals(listOf(3L, 4L, 1L, 2L, 5L), viewState.tracks.map { it.id })
    }

    @Test
    fun `Tracks should be sorted by rank if selection change count is same`() {
        val tracks = listOf(
            TrackWithProgress.Companion.stub(trackId = 1L, rank = 2),
            TrackWithProgress.Companion.stub(trackId = 2L, rank = 1),
            TrackWithProgress.Companion.stub(trackId = 3L, rank = 3)
        )
        val state = State.Content(
            tracks = tracks,
            selectedTrackId = null,
            tracksSelectionCountMap = emptyMap()
        )

        val viewState = viewStateMapper.map(state) as TrackSelectionListFeature.ViewState.Content

        assertEquals(listOf(2L, 1L, 3L), viewState.tracks.map { it.id })
    }

    @Test
    fun `Selected track should be first then sort by selection change count and then by rank`() {
        val selectedTrackId = 3L
        val tracks = listOf(
            TrackWithProgress.Companion.stub(trackId = 1L, rank = 2),
            TrackWithProgress.Companion.stub(trackId = 2L, rank = 1),
            TrackWithProgress.Companion.stub(trackId = selectedTrackId, rank = 3),
            TrackWithProgress.Companion.stub(trackId = 4L, rank = 4),
            TrackWithProgress.Companion.stub(trackId = 5L, rank = 5)
        )
        val tracksSelectionCountMap = mapOf(
            1L to 2,
            2L to 3,
            4L to 1,
            5L to 4
        )
        val state = State.Content(
            tracks = tracks,
            selectedTrackId = selectedTrackId,
            tracksSelectionCountMap = tracksSelectionCountMap
        )

        val viewState = viewStateMapper.map(state) as TrackSelectionListFeature.ViewState.Content

        val expectedOrder = listOf(selectedTrackId, 5L, 2L, 1L, 4L)
        assertEquals(expectedOrder, viewState.tracks.map { it.id })
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
                selectedTrackId = selectedTrackId,
                tracksSelectionCountMap = emptyMap()
            ),
            Message.TrackClicked(trackId)
        )
        assertContains(
            actions,
            Action.ViewAction.NavigateTo.TrackDetails(trackWithProgress, false, isNewUserMode = false)
        )
    }
}