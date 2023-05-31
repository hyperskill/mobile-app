package org.hyperskill.track_selection.details

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.domain.model.TrackWithProgress
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ContentState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.InternalAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsReducer
import org.hyperskill.app.track_selection.details.view.TrackSelectionDetailsViewStateMapper
import org.hyperskill.profile.stub
import org.hyperskill.providers.stub
import org.hyperskill.track.stub
import org.hyperskill.track_selection.stub

class TrackSelectionDetailsTest {

    private val reducer = TrackSelectionDetailsReducer()

    private val resourceProvider = ResourceProviderStub()

    private val viewStateMapper = TrackSelectionDetailsViewStateMapper(
        resourceProvider = resourceProvider,
        dateFormatter = SharedDateFormatter(resourceProvider)
    )

    @Test
    fun `Initialize message should trigger providers and freemium info loading`() {
        val trackWithProgress = TrackWithProgress.stub(
            topicsProviders = listOf(1L, 2L, 3L)
        )
        val (state, actions) = reducer.reduce(
            TrackSelectionDetailsFeature.initialState(
                trackWithProgress = trackWithProgress,
                isTrackSelected = false,
                isNewUserMode = false
            ),
            Message.Initialize
        )

        assertContains(
            actions,
            InternalAction.FetchAdditionalInfo(
                providerIds = trackWithProgress.track.topicProviders,
                forceLoadFromNetwork = false
            )
        )
        assertEquals(
            ContentState.Loading,
            state.contentState
        )
    }

    @Test
    fun `FetchContent success message should update content`() {
        val providersIds = listOf(1L, 2L, 3L)
        val trackWithProgress = TrackWithProgress.stub(
            topicsProviders = providersIds
        )
        val providers = providersIds.map {
            Provider.stub(id = it)
        }
        val profile = Profile.stub()
        val subscriptionType = SubscriptionType.UNKNOWN
        val (state, _) = reducer.reduce(
            TrackSelectionDetailsFeature.initialState(
                trackWithProgress = trackWithProgress,
                isTrackSelected = false,
                isNewUserMode = false
            ),
            TrackSelectionDetailsFeature.FetchAdditionalInfoResult.Success(
                providers = providers,
                profile = profile,
                subscriptionType = subscriptionType
            )
        )

        assertEquals(
            ContentState.Content(
                providers = providers,
                profile = profile,
                subscriptionType = subscriptionType
            ),
            state.contentState
        )
    }

    @Test
    fun `RetryContentLoading message should trigger force providers and freemium info loading`() {
        val trackWithProgress = TrackWithProgress.stub(
            topicsProviders = listOf(1L, 2L, 3L)
        )
        val (state, actions) = reducer.reduce(
            TrackSelectionDetailsFeature.State(
                trackWithProgress = trackWithProgress,
                isTrackSelected = false,
                isNewUserMode = false,
                isTrackLoadingShowed = false,
                contentState = ContentState.NetworkError
            ),
            Message.RetryContentLoading
        )
        assertContains(
            actions,
            InternalAction.FetchAdditionalInfo(
                providerIds = trackWithProgress.track.topicProviders,
                forceLoadFromNetwork = true
            )
        )
        assertEquals(
            ContentState.Loading,
            state.contentState
        )
    }

    @Test
    fun `SelectTrackButtonClicked message should trigger track selection`() {
        val trackId = 0L
        val contentState = ContentState.Content(
            providers = emptyList(),
            profile = Profile.stub(),
            subscriptionType = SubscriptionType.UNKNOWN
        )
        val (state, actions) = reducer.reduce(
            TrackSelectionDetailsFeature.State(
                trackWithProgress = TrackWithProgress.stub(trackId = trackId),
                isTrackSelected = false,
                isNewUserMode = false,
                isTrackLoadingShowed = false,
                contentState = contentState
            ),
            Message.SelectTrackButtonClicked
        )
        assertEquals(state.isTrackLoadingShowed, true)
        assertEquals(state.contentState, contentState)
        assertContains(
            actions,
            InternalAction.SelectTrack(trackId)
        )
    }

    @Test
    fun `Successful track selection should trigger navigation to StudyPlan or Home screen`() {
        listOf(true, false).forEach { isNewUserMode ->
            val (state, actions) = reducer.reduce(
                TrackSelectionDetailsFeature.State(
                    trackWithProgress = TrackWithProgress.stub(),
                    isTrackSelected = false,
                    isNewUserMode = isNewUserMode,
                    isTrackLoadingShowed = false,
                    contentState = ContentState.Content(
                        providers = emptyList(),
                        profile = Profile.stub(),
                        subscriptionType = SubscriptionType.UNKNOWN
                    )
                ),
                TrackSelectionDetailsFeature.TrackSelectionResult.Success
            )
            assertEquals(state.isTrackLoadingShowed, false)
            assertContains(
                actions,
                TrackSelectionDetailsFeature.Action.ViewAction.ShowTrackSelectionStatus.Success
            )
            assertContains(
                actions,
                if (isNewUserMode) {
                    TrackSelectionDetailsFeature.Action.ViewAction.NavigateTo.Home
                } else {
                    TrackSelectionDetailsFeature.Action.ViewAction.NavigateTo.StudyPlan
                }
            )
        }
    }

    @Test
    fun `Successful track selection should trigger navigation to ProjectSelection or Home for users with subscription`() { // ktlint-disable max-line-length
        listOf(
            SubscriptionType.PREMIUM,
            SubscriptionType.PERSONAL,
            SubscriptionType.TRIAL
        ).forEach { subscriptionType ->
            listOf(
                listOf(1L, 2L, 3L),
                emptyList()
            ).forEach { projects ->
                val trackId = 0L
                val (state, actions) = reducer.reduce(
                    TrackSelectionDetailsFeature.State(
                        trackWithProgress = TrackWithProgress(
                            track = Track.stub(
                                id = trackId,
                                projects = projects
                            ),
                            trackProgress = TrackProgress.stub(trackId = trackId)
                        ),
                        isTrackSelected = false,
                        isNewUserMode = true,
                        isTrackLoadingShowed = false,
                        contentState = ContentState.Content(
                            providers = emptyList(),
                            profile = Profile.stub(),
                            subscriptionType = subscriptionType
                        )
                    ),
                    TrackSelectionDetailsFeature.TrackSelectionResult.Success
                )
                assertEquals(state.isTrackLoadingShowed, false)
                assertContains(
                    actions,
                    TrackSelectionDetailsFeature.Action.ViewAction.ShowTrackSelectionStatus.Success
                )
                assertContains(
                    actions,
                    if (projects.isEmpty()) {
                        TrackSelectionDetailsFeature.Action.ViewAction.NavigateTo.Home
                    } else {
                        TrackSelectionDetailsFeature.Action.ViewAction.NavigateTo.ProjectSelectionList(
                            trackId = trackId,
                            isNewUserMode = true
                        )
                    }
                )
            }
        }
    }

    @Test
    fun `Failed track selection should trigger error showing`() {
        val (state, actions) = reducer.reduce(
            TrackSelectionDetailsFeature.State(
                trackWithProgress = TrackWithProgress.stub(),
                isTrackSelected = false,
                isNewUserMode = false,
                isTrackLoadingShowed = false,
                contentState = ContentState.Content(
                    providers = emptyList(),
                    profile = Profile.stub(),
                    subscriptionType = SubscriptionType.UNKNOWN
                )
            ),
            TrackSelectionDetailsFeature.TrackSelectionResult.Error
        )
        assertEquals(state.isTrackLoadingShowed, false)
        assertContains(
            actions,
            TrackSelectionDetailsFeature.Action.ViewAction.ShowTrackSelectionStatus.Error
        )
    }

    @Test
    fun `Certificate and projects info should not be available for freemium user`() {
        val state = TrackSelectionDetailsFeature.State(
            trackWithProgress = TrackWithProgress.stub(),
            isTrackSelected = false,
            isNewUserMode = false,
            isTrackLoadingShowed = false,
            contentState = ContentState.Content(
                providers = emptyList(),
                profile = Profile.stub(),
                subscriptionType = SubscriptionType.FREEMIUM
            )
        )

        val viewState = viewStateMapper.map(state)

        assertFalse {
            (viewState as ViewState.Content).isCertificateAvailable
        }
        assertNull(
            (viewState as ViewState.Content).formattedProjectsCount
        )
    }

    @Test
    fun `Certificate and projects info should be available for non freemium user`() {
        val state = TrackSelectionDetailsFeature.State(
            trackWithProgress = TrackWithProgress.stub(canIssueCertificate = true),
            isTrackSelected = false,
            isNewUserMode = false,
            isTrackLoadingShowed = false,
            contentState = ContentState.Content(
                providers = emptyList(),
                profile = Profile.stub(),
                subscriptionType = SubscriptionType.UNKNOWN
            )
        )

        val viewState = viewStateMapper.map(state)

        assertTrue {
            (viewState as ViewState.Content).isCertificateAvailable
        }
        assertNotNull(
            (viewState as ViewState.Content).formattedProjectsCount
        )
    }
}