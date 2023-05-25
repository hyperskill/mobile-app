package org.hyperskill.app.track_selection.details.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.progresses.domain.model.averageRating
import org.hyperskill.app.track.domain.model.totalTopicsCount
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ContentState

internal class TrackSelectionDetailsViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: TrackSelectionDetailsFeature.State): TrackSelectionDetailsFeature.ViewState =
        when (state.contentState) {
            ContentState.Idle -> TrackSelectionDetailsFeature.ViewState.Idle
            ContentState.Loading -> TrackSelectionDetailsFeature.ViewState.Loading
            is ContentState.Content -> mapContentStateToViewState(state, state.contentState)
            ContentState.NetworkError -> TrackSelectionDetailsFeature.ViewState.NetworkError
        }

    private fun mapContentStateToViewState(
        state: TrackSelectionDetailsFeature.State,
        contentState: ContentState.Content
    ): TrackSelectionDetailsFeature.ViewState.Content =
        with(state.trackWithProgress) {
            TrackSelectionDetailsFeature.ViewState.Content(
                title = track.title,
                description = track.description.takeIf { it.isNotBlank() },
                formattedRating = formatRating(trackProgress.averageRating()),
                formattedTimeToComplete = formatTimeToComplete(track.secondsToComplete),
                formattedTopicsCount = formatTopicsCount(track.totalTopicsCount),
                formattedProjectsCount = formatProjectsCount(contentState.isFreemiumEnabled, track.projects.size),
                isCertificateAvailable = !contentState.isFreemiumEnabled && track.canIssueCertificate,
                mainProvider = contentState.providers
                    .firstOrNull { it.id == track.providerId }
                    ?.let { mainProvider ->
                        TrackSelectionDetailsFeature.ViewState.MainProvider(
                            title = mainProvider.title,
                            description = mainProvider.description
                        )
                    },
                formattedOtherProviders = contentState.providers
                    .filter { it.id != track.providerId }
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString(", ") { it.title },
                isBeta = track.isBeta,
                isCompleted = track.isCompleted,
                isSelected = state.isTrackSelected,
                isSelectTrackButtonEnabled = !state.isTrackSelected && !track.isCompleted,
                isTrackSelectionLoadingShowed = state.isTrackLoadingShowed
            )
        }

    private fun formatRating(rating: Double): String =
        if (rating > 0.0) {
            NumbersFormatter.formatFloatingNumber(rating, 1)
        } else {
            resourceProvider.getString(SharedResources.strings.track_selection_details_no_rating)
        }

    private fun formatTimeToComplete(secondsToComplete: Float?): String? =
        dateFormatter.formatHoursCount(secondsToComplete)?.let { formattedTime ->
            resourceProvider.getString(
                SharedResources.strings.track_selection_details_time_to_complete_text_template,
                formattedTime
            )
        }

    private fun formatTopicsCount(topicsCount: Int): String =
        resourceProvider.getString(
            SharedResources.strings.track_selection_details_topics_count_text_template,
            resourceProvider.getQuantityString(SharedResources.plurals.topics, topicsCount, topicsCount)
        )

    private fun formatProjectsCount(
        isFreemiumEnabled: Boolean,
        projectsCount: Int
    ): String? =
        if (!isFreemiumEnabled) {
            resourceProvider.getString(
                SharedResources.strings.track_selection_details_projects_text_template,
                resourceProvider.getQuantityString(SharedResources.plurals.projects, projectsCount, projectsCount)
            )
        } else {
            null
        }
}