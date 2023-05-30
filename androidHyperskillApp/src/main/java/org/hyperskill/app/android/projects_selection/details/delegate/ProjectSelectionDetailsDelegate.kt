package org.hyperskill.app.android.projects_selection.details.delegate

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.FragmentManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.databinding.FragmentProjectSelectionDetailsBinding
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature
import org.hyperskill.app.projects.domain.model.ProjectLevel
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object ProjectSelectionDetailsDelegate {
    fun render(
        context: Context,
        fragmentManager: FragmentManager,
        viewBinding: FragmentProjectSelectionDetailsBinding,
        state: ProjectSelectionDetailsFeature.ViewState.Content
    ) {
        updateContentBottomMargin(context, viewBinding)
        viewBinding.projectSelectionDetailsToolbar.title = state.formattedTitle
        viewBinding.projectSelectionDetailsSelectButton.isEnabled = state.isSelectProjectButtonEnabled
        renderSelectionLoading(fragmentManager, state.isSelectProjectLoadingShowed)

        with(viewBinding.projectSelectionDetailsDescription) {
            projectSelectionLearningOutcomesDescription.setText(state.learningOutcomesDescription)
            projectSelectionDetailsLearningOutcomesTitle.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                updateMargins(
                    top = if (state.areTagsVisible) {
                        context.resources.getDimensionPixelOffset(R.dimen.project_selection_list_item_tags_top_margin)
                    } else {
                        0
                    }
                )
            }
            projectSelectionDetailsSelectedBadge.root.isVisible = state.isSelected
            projectSelectionDetailsBestRatingBadge.root.isVisible = state.isBestRated
            projectSelectionDetailsFastestToCompleteBadge.root.isVisible = state.isFastestToComplete
            projectSelectionDetailsBetaBadge.root.isVisible = state.isBeta
            projectSelectionDetailsIdeRequiredBadge.root.isVisible = state.isIdeRequired
        }

        with(viewBinding.projectSelectionDetailsProjectOverview) {
            projectOverviewRatingTextView.setTextIfChanged(state.formattedAverageRating)
            setNullableText(projectOverviewTimeToCompleteTextView, state.formattedTimeToComplete)
            setNullableText(projectOverviewCertificateAvailabilityTextView, state.formattedGraduateDescription)

            val projectLevel = state.projectLevel
            if (projectLevel != null) {
                projectOverviewLevelTextView.setCompoundDrawablesWithIntrinsicBounds(
                    /* left = */ getDrawableByProjectLevel(context, projectLevel),
                    /* top = */ null,
                    /* right = */ null,
                    /* bottom = */ null
                )
            }
            setNullableText(projectOverviewLevelTextView, state.formattedProjectLevel)
        }

        with(viewBinding.projectSelectionDetailsProviders) {
            root.isVisible = state.providerName != null
            setNullableText(projectSelectionDetailsMainProviderTitle, state.providerName)
        }
    }

    private fun getDrawableByProjectLevel(context: Context, projectLevel: ProjectLevel): Drawable? =
        when (projectLevel) {
            ProjectLevel.EASY -> R.drawable.ic_project_details_level_easy
            ProjectLevel.MEDIUM -> R.drawable.ic_project_details_medium
            ProjectLevel.HARD -> R.drawable.ic_project_details_hard
            ProjectLevel.NIGHTMARE -> R.drawable.ic_project_details_challenging
        }.let {
            ContextCompat.getDrawable(context, it)
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
        viewBinding: FragmentProjectSelectionDetailsBinding
    ) {
        viewBinding.projectSelectionDetailsSelectButton.doOnNextLayout {
            viewBinding.projectSelectionDetailsContentContainer.updateLayoutParams<ViewGroup.MarginLayoutParams> {
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