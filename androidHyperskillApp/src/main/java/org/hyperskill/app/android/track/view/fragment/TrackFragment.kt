package org.hyperskill.app.android.track.view.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.load
import coil.size.Scale
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.topics.view.delegate.TopicsToDiscoverNextDelegate
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextFeature
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import kotlin.math.roundToInt

class TrackFragment :
    Fragment(R.layout.fragment_track),
    ReduxView<TrackFeature.State, TrackFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            TrackFragment()
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentTrackBinding by viewBinding(FragmentTrackBinding::bind)
    private val trackViewModel: TrackViewModel by reduxViewModel(this) { viewModelFactory }

    private val viewStateDelegate: ViewStateDelegate<TrackFeature.TrackState> = ViewStateDelegate()
    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null

    private val topicsDelegate: TopicsToDiscoverNextDelegate by lazy(LazyThreadSafetyMode.NONE) {
        TopicsToDiscoverNextDelegate(loadingItems = 3) { topicId ->
            val messageToWrap = TopicsToDiscoverNextFeature.Message.TopicToDiscoverNextClicked(topicId)
            trackViewModel.onNewMessage(TrackFeature.Message.TopicsToDiscoverNextMessage(messageToWrap))
        }
    }

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        initGamificationToolbarDelegate()
        viewBinding.trackError.tryAgain.setOnClickListener {
            trackViewModel.onNewMessage(TrackFeature.Message.Initialize(forceUpdate = true))
        }
        topicsDelegate.setup(requireContext(), viewBinding.trackNextTopicsRecyclerView)
        trackViewModel.onNewMessage(TrackFeature.Message.Initialize())
        trackViewModel.onNewMessage(TrackFeature.Message.ViewedEventMessage)
    }

    private fun injectComponents() {
        val trackComponent = HyperskillApp.graph().buildTrackComponent()
        val platformTrackComponent = HyperskillApp.graph().buildPlatformTrackComponent(trackComponent)
        viewModelFactory = platformTrackComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        with(viewStateDelegate) {
            addState<TrackFeature.TrackState.Idle>()
            addState<TrackFeature.TrackState.Loading>(
                viewBinding.trackHeaderSkeleton.root,
                viewBinding.trackFooterSkeleton.root
            )
            addState<TrackFeature.TrackState.NetworkError>(viewBinding.trackError.root)
            addState<TrackFeature.TrackState.Content>(
                viewBinding.trackIconImageView,
                viewBinding.trackNameTextView,
                viewBinding.trackLearningTextView,
                viewBinding.trackProgress.root,
                viewBinding.trackAbout.root
            )
        }
    }

    private fun initGamificationToolbarDelegate() {
        viewBinding.trackAppBar.gamificationCollapsingToolbarLayout.title =
            requireContext().getString(org.hyperskill.app.R.string.track_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            viewLifecycleOwner,
            requireContext(),
            viewBinding.trackAppBar,
            GamificationToolbarScreen.TRACK
        ) { message ->
            trackViewModel.onNewMessage(TrackFeature.Message.GamificationToolbarMessage(message))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamificationToolbarDelegate = null
    }

    override fun onAction(action: TrackFeature.Action.ViewAction) {
        when (action) {
            is TrackFeature.Action.ViewAction.OpenUrl ->
                requireContext().openUrl(action.url)
            is TrackFeature.Action.ViewAction.ShowGetMagicLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
            is TrackFeature.Action.ViewAction.GamificationToolbarViewAction ->
                when (action.viewAction) {
                    is GamificationToolbarFeature.Action.ViewAction.ShowProfileTab ->
                        mainScreenRouter.switch(ProfileScreen(isInitCurrent = true))
                }
            is TrackFeature.Action.ViewAction.TopicsToDiscoverNextViewAction ->
                when (action.viewAction) {
                    is TopicsToDiscoverNextFeature.Action.ViewAction.ShowStepScreen -> {
                        val viewAction =
                            action.viewAction as TopicsToDiscoverNextFeature.Action.ViewAction.ShowStepScreen
                        requireRouter().navigateTo(StepScreen(StepRoute.Learn(viewAction.stepId)))
                    }
                }
        }
    }

    override fun render(state: TrackFeature.State) {
        viewStateDelegate.switchState(state.trackState)
        TransitionManager.beginDelayedTransition(viewBinding.root, AutoTransition())
        val trackState = state.trackState
        if (trackState is TrackFeature.TrackState.Content) {
            renderContent(trackState)
        }
        gamificationToolbarDelegate?.render(state.toolbarState)
        renderNextTopics(state.topicsToDiscoverNextState)
    }

    private fun renderContent(content: TrackFeature.TrackState.Content) {
        if (content.isLoadingMagicLink) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
        }
        renderTrackCoverAndName(content.track)
        renderCards(content)
        renderAboutSection(content)
    }

    private fun renderTrackCoverAndName(track: Track) {
        if (track.cover != null) {
            viewBinding.trackIconImageView.load(track.cover, imageLoader) {
                scale(Scale.FILL)
            }
        } else {
            viewBinding.trackIconImageView.visibility = View.GONE
        }
        viewBinding.trackNameTextView.text = track.title
    }

    private fun renderCards(content: TrackFeature.TrackState.Content) {
        with(viewBinding.trackProgress) {
            if (content.studyPlan != null) {
                trackTimeToCompleteTextView.text =
                    if (content.studyPlan!!.hoursToReachTrack != 0) {
                        "~ ${content.studyPlan!!.hoursToReachTrack} h"
                    } else {
                        "~ ${content.studyPlan!!.minutesToReachTrack} m"
                    }
            } else {
                trackProgressTimeCardView.visibility = View.GONE
                (trackProgressCompletedGraduateProjectsCardView.layoutParams as ViewGroup.MarginLayoutParams)
                    .setMargins(0, 0, 0, 0)
            }

            trackCompletedTopicsTextView.text = "${content.trackProgress.completedTopics} / ${content.track.topicsCount}"
            trackCompletedTopicsProgressIndicator.progress =
                if (content.track.topicsCount == 0) {
                    0
                } else {
                    content.trackProgress.completedTopics * 100 / content.track.topicsCount
                }

            if (content.track.capstoneProjects.isEmpty()) {
                trackProgressCompletedGraduateProjectsCardView.visibility = View.GONE
            } else {
                trackCompletedGraduateProjectsTextView.text = "${content.trackProgress.completedCapstoneProjects.size}"
            }

            if (content.track.capstoneTopicsCount == 0) {
                trackAppliedCoreTopicsCardView.visibility = View.GONE
            } else {
                trackAppliedCoreTopicsTextView.text =
                    "${content.trackProgress.appliedCapstoneTopicsCount} / ${content.track.capstoneTopicsCount}"
                trackAppliedCoreTopicsProgressIndicator.progress =
                    content.trackProgress.appliedCapstoneTopicsCount * 100 / content.track.capstoneTopicsCount
            }
        }
    }

    private fun renderAboutSection(content: TrackFeature.TrackState.Content) {
        with(viewBinding.trackAbout) {
            trackAboutUsefulnessTextView.text = "${content.trackProgress.averageRating}"
            val hoursToComplete = (content.track.secondsToComplete / 3600).roundToInt()
            trackAboutAllPerformTimeTextView.text = resources.getQuantityString(
                org.hyperskill.app.R.plurals.hours,
                hoursToComplete,
                hoursToComplete
            )

            if (content.track.projects.isEmpty()) {
                trackAboutProjectsCountTextView.visibility = View.GONE
            } else {
                trackAboutProjectsCountTextView.text = resources.getQuantityString(
                    org.hyperskill.app.R.plurals.projects,
                    content.track.projects.size,
                    content.track.projects.size
                )
            }

            if (content.track.topicsCount == 0) {
                trackAboutTopicsCountTextView.visibility = View.GONE
            } else {
                trackAboutTopicsCountTextView.text = resources.getQuantityString(
                    org.hyperskill.app.R.plurals.topics,
                    content.track.topicsCount,
                    content.track.topicsCount
                )
            }

            trackAboutDescriptionTextView.text = content.track.description

            trackAboutKeepYourProgressInWebTextView.paintFlags =
                trackAboutKeepYourProgressInWebTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            trackAboutKeepYourProgressInWebTextView.setOnClickListener {
                trackViewModel.onNewMessage(TrackFeature.Message.ClickedContinueInWeb)
            }
        }
    }

    private fun renderNextTopics(state: TopicsToDiscoverNextFeature.State) {
        viewBinding.trackTopicsTitle.isVisible =
            state is TopicsToDiscoverNextFeature.State.Content
        topicsDelegate.render(state)
    }
}