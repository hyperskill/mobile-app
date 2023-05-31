package org.hyperskill.app.android.track_selection.details.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackSelectionDetailsBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.projects_selection.list.navigation.ProjectSelectionListScreen
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import org.hyperskill.app.android.track_selection.details.delegate.TrackSelectionDetailsDelegate
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ViewState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
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
        with(viewBinding.projectSelectionDetailsToolbar) {
            navigationIcon = if (params.isNewUserMode) {
                null
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_toolbar_back)
            }
            setNavigationOnClickListener {
                requireRouter().exit()
            }
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
            ViewAction.NavigateTo.StudyPlan -> {
                requireRouter().backTo(MainScreen)
                mainScreenRouter.switch(StudyPlanScreen)
            }
            ViewAction.NavigateTo.Home ->
                requireRouter().newRootScreen(MainScreen)
            is ViewAction.NavigateTo.ProjectSelectionList ->
                requireRouter().newRootScreen(
                    ProjectSelectionListScreen(
                        ProjectSelectionListParams(
                            trackId = action.trackId,
                            isNewUserMode = action.isNewUserMode
                        )
                    )
                )
            ViewAction.ShowTrackSelectionStatus.Success ->
                view?.snackbar(org.hyperskill.app.R.string.track_selection_details_selection_succeed)
            ViewAction.ShowTrackSelectionStatus.Error ->
                view?.snackbar(org.hyperskill.app.R.string.track_selection_details_selection_failed)
        }
    }

    override fun render(state: ViewState) {
        viewStateDelegate?.switchState(state)
        if (state is ViewState.Content) {
            TrackSelectionDetailsDelegate.render(
                context = requireContext(),
                viewBinding = viewBinding,
                fragmentManager = childFragmentManager,
                state = state
            )
        }
    }
}