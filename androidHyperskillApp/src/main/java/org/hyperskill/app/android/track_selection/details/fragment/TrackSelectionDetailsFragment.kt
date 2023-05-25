package org.hyperskill.app.android.track_selection.details.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackSelectionDetailsBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TrackSelectionDetailsFragment :
    Fragment(R.layout.fragment_track_selection_details),
    ReduxView<ViewState, ViewAction> {

    companion object {
        fun newInstance(params: TrackSelectionDetailsParams): TrackSelectionDetailsFragment =
            TrackSelectionDetailsFragment().apply {
                this.params = params
            }
    }

    private var viewModelProvider: ViewModelProvider.Factory? = null
    private val trackSelectionDetailsViewModel: TrackSelectionDetailsViewModel by reduxViewModel(this) {
        requireNotNull(viewModelProvider) { "ViewModelFactory must be initialized" }
    }

    private val mainScreenRouter: MainScreenRouter by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router
    }

    private val viewBinding: FragmentTrackSelectionDetailsBinding by viewBinding(
        FragmentTrackSelectionDetailsBinding::bind
    )

    private var params: TrackSelectionDetailsParams by argument(serializer = TrackSelectionDetailsParams.serializer())

    private var viewStateDelegate: ViewStateDelegate<ViewState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val component =
            HyperskillApp.graph().buildPlatformTrackSelectionDetailsComponent(params)
        viewModelProvider = component.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        trackSelectionDetailsViewModel.onNewMessage(Message.ViewedEventMessage)

        setupViewStateDelegate()
        viewBinding.projectSelectionDetailsToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.trackSelectionDetailsError.tryAgain.setOnClickListener {
            trackSelectionDetailsViewModel.onNewMessage(Message.RetryContentLoading)
        }
        viewBinding.trackSelectionDetailsSelectButton.setOnClickListener {
            trackSelectionDetailsViewModel.onNewMessage(Message.SelectTrackButtonClicked)
        }
    }

    private fun setupViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<ViewState>().apply {
            addState<ViewState.Idle>()
            addState<ViewState.Loading>(
                viewBinding.trackSelectionDetailsScreenLinearLayout,
                viewBinding.trackSelectionDetailsSkeleton.root
            )
            addState<ViewState.NetworkError>(viewBinding.trackSelectionDetailsError.root)
            addState<ViewState.Content>(
                viewBinding.trackSelectionDetailsScreenLinearLayout,
                viewBinding.trackSelectionDetailsContentContainer,
                viewBinding.trackSelectionDetailsSelectButton
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NavigateTo.StudyPlan ->
                mainScreenRouter.switch(StudyPlanScreen)
            ViewAction.ShowTrackSelectionStatus.Success ->
                view?.snackbar(org.hyperskill.app.R.string.track_selection_details_selection_succeed)
            ViewAction.ShowTrackSelectionStatus.Error ->
                view?.snackbar(org.hyperskill.app.R.string.common_error)
        }
    }

    override fun render(state: ViewState) {
        viewStateDelegate?.switchState(state)
        if (state is ViewState.Content) {
            updateContentBottomMargin()
            renderSelectionLoading(state.isTrackSelectionLoadingShowed)
            viewBinding.projectSelectionDetailsToolbar.title = state.title
            with(viewBinding.trackSelectionDetailsDescription) {
                setNullableText(trackSelectionDetailsDescription, state.description)
                trackSelectionDetailsDescription.updateLayoutParams<MarginLayoutParams> {
                    updateMargins(
                        top = if (state.areTagsVisible) {
                            resources.getDimensionPixelOffset(R.dimen.track_selection_details_tags_bottom_margin)
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
    }

    private fun renderSelectionLoading(isLoadingShowed: Boolean) {
        if (isLoadingShowed) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
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
    private fun updateContentBottomMargin() {
        viewBinding.trackSelectionDetailsSelectButton.doOnNextLayout {
            viewBinding.trackSelectionDetailsContentContainer.updateLayoutParams<MarginLayoutParams> {
                updateMargins(
                    bottom = it.height + it.marginBottom +
                        resources.getDimensionPixelOffset(R.dimen.track_selection_details_content_bottom_margin)
                )
            }
        }
    }
}