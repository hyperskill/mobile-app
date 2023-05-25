package org.hyperskill.app.android.track_selection.details.delegate

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.FragmentManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.databinding.FragmentTrackSelectionDetailsBinding
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object TrackSelectionDetailsDelegate {

    fun render(
        context: Context,
        viewBinding: FragmentTrackSelectionDetailsBinding,
        fragmentManager: FragmentManager,
        state: TrackSelectionDetailsFeature.ViewState.Content
    ) {
        updateContentBottomMargin(context, viewBinding)
        renderSelectionLoading(fragmentManager, state.isTrackSelectionLoadingShowed)
        viewBinding.projectSelectionDetailsToolbar.title = state.title
        with(viewBinding.trackSelectionDetailsDescription) {
            setNullableText(trackSelectionDetailsDescription, state.description)
            trackSelectionDetailsDescription.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                updateMargins(
                    top = if (state.areTagsVisible) {
                        context.resources.getDimensionPixelOffset(R.dimen.track_selection_details_tags_bottom_margin)
                    } else {
                        0
                    }
                )
            }
            trackSelectionDetailsSelectedBadge.root.isVisible = state.isSelected
            trackSelectionDetailsBetaBadge.root.isVisible = state.isBeta
            trackSelectionDetailsCompletedBadge.root.isVisible = state.isCompleted
        }
        with(viewBinding.trackSelectionDetailsTrackOverview) {
            trackOverviewRatingTextView.setTextIfChanged(state.formattedRating)
            setNullableText(trackOverviewHoursToCompleteTextView, state.formattedTimeToComplete)
            trackOverviewTopicsCountTextView.setTextIfChanged(state.formattedTopicsCount)
            setNullableText(trackOverviewProjectsCountTextView, state.formattedProjectsCount)
            trackOverviewCertificateAvailabilityTextView.isVisible = state.isCertificateAvailable
        }
        with(viewBinding.trackSelectionDetailsProviders) {
            setNullableText(trackSelectionDetailsMainProviderTitle, state.mainProvider?.title)
            setNullableText(trackSelectionDetailsMainProviderDescription, state.mainProvider?.description)
            trackSelectionDetailsMainProviderDivider.isVisible = state.formattedOtherProviders != null
            trackSelectionDetailsOtherProvidersTitle.isVisible = state.formattedOtherProviders != null
            setNullableText(trackSelectionDetailsOtherProvidersDescription, state.formattedOtherProviders)
        }
    }

    private fun setNullableText(textView: TextView, text: String?) {
        textView.isVisible = text != null
        if (text != null) {
            textView.setTextIfChanged(text)
        }
    }

    /**
     * Add bottom margin to content container to prevent overlapping with select button
     */
    private fun updateContentBottomMargin(
        context: Context,
        viewBinding: FragmentTrackSelectionDetailsBinding
    ) {
        viewBinding.trackSelectionDetailsSelectButton.doOnNextLayout {
            viewBinding.trackSelectionDetailsContentContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                updateMargins(
                    bottom = it.height + it.marginBottom +
                        context.resources.getDimensionPixelOffset(R.dimen.track_selection_details_content_bottom_margin)
                )
            }
        }
    }

    private fun renderSelectionLoading(fragmentManager: FragmentManager, isLoadingShowed: Boolean) {
        if (isLoadingShowed) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(fragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            fragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
        }
    }
}