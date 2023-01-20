package org.hyperskill.app.android.track.view.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackBinding
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
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
    private val viewStateDelegate: ViewStateDelegate<TrackFeature.State> = ViewStateDelegate()

    private val nextTopicsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<Topic>().apply {
            addDelegate(nextTopicAdapterDelegate())
        }
    }

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.trackError.tryAgain.setOnClickListener {
            trackViewModel.onNewMessage(TrackFeature.Message.Initialize(forceUpdate = true))
        }
        setupTopicsRecycler()
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
            addState<TrackFeature.State.Idle>()
            addState<TrackFeature.State.Loading>(viewBinding.trackSkeleton.root)
            addState<TrackFeature.State.NetworkError>(viewBinding.trackError.root)
            addState<TrackFeature.State.Content>(viewBinding.trackContainer)
        }
    }

    override fun onAction(action: TrackFeature.Action.ViewAction) {
        when (action) {
            is TrackFeature.Action.ViewAction.NavigateTo.StepScreen ->
                requireRouter().navigateTo(StepScreen(StepRoute.Learn(action.stepId)))
            is TrackFeature.Action.ViewAction.OpenUrl ->
                requireContext().openUrl(action.url)
            is TrackFeature.Action.ViewAction.ShowGetMagicLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
            else -> {}
        }
    }

    private fun setupTopicsRecycler() {
        with(viewBinding.trackNextTopicsRecyclerView) {
            adapter = nextTopicsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            val verticalMargin = resources.getDimensionPixelSize(R.dimen.track_next_topic_vertical_item_margin)
            addItemDecoration(
                VerticalMarginItemDecoration(
                    verticalMargin = verticalMargin,
                    firstItemTopMargin = verticalMargin,
                    lastItemMargin = verticalMargin
                )
            )
            addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.track_next_topic_horizontal_item_margin)
                )
            )
        }
    }

    override fun render(state: TrackFeature.State) {
        viewStateDelegate.switchState(state)
        TransitionManager.beginDelayedTransition(viewBinding.root, AutoTransition())
        if (state is TrackFeature.State.Content) {
            renderContent(state)
        }
    }

    private fun renderContent(content: TrackFeature.State.Content) {
        if (content.isLoadingMagicLink) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
        }
        renderTrackCoverAndName(content.track)
        renderCards(content)
        renderAboutSection(content)
        renderNextTopics(content.topicsToDiscoverNext)
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

    private fun renderCards(content: TrackFeature.State.Content) {
        with(viewBinding) {
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

    private fun renderAboutSection(content: TrackFeature.State.Content) {
        with(viewBinding) {
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

    private fun renderNextTopics(topics: List<Topic>) {
        viewBinding.trackTopicsLinearLayout.isVisible = topics.isNotEmpty()
        if (topics.isNotEmpty()) {
            nextTopicsAdapter.items = topics
        }
    }

    private fun nextTopicAdapterDelegate() =
        adapterDelegate<Topic, Topic>(
            R.layout.item_track_next_topic
        ) {
            val title = itemView.findViewById<TextView>(R.id.nextTopicTitle)
            itemView.setOnClickListener {
                item?.let { topic ->
                    trackViewModel.onNewMessage(
                        TrackFeature.Message.TopicToDiscoverNextClicked(topicId = topic.id)
                    )
                }
            }

            onBind { topic ->
                title.text = topic.title
            }
        }
}