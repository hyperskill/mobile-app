package org.hyperskill.app.android.track.view.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.size.Scale
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.launchUrl
import org.hyperskill.app.android.databinding.FragmentTrackBinding
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
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

    private lateinit var track: Track
    private lateinit var trackProgress: TrackProgress
    private var studyPlan: StudyPlan? = null

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
            addState<TrackFeature.State.Loading>(viewBinding.trackProgressBar)
            addState<TrackFeature.State.NetworkError>(viewBinding.trackError.root)
            addState<TrackFeature.State.Content>(viewBinding.trackContainer)
        }
    }

    override fun onAction(action: TrackFeature.Action.ViewAction) {
        when (action) {
            is TrackFeature.Action.ViewAction.FollowLink ->
                requireContext().launchUrl(action.url)
        }
    }

    override fun render(state: TrackFeature.State) {
        viewStateDelegate.switchState(state)

        if (state is TrackFeature.State.Content) {
            track = state.track
            trackProgress = state.trackProgress
            studyPlan = state.studyPlan

            setupTrack()
        }
    }

    private fun setupTrack() {
        setupTrackCoverAndName()
        setupCards()
        setupAboutSection()
    }

    private fun setupTrackCoverAndName() {
        val svgImageLoader = ImageLoader.Builder(requireContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        if (track.cover != null) {
            viewBinding.trackIconImageView.load(track.cover, svgImageLoader) {
                scale(Scale.FILL)
            }
        } else {
            viewBinding.trackIconImageView.visibility = View.GONE
        }
        viewBinding.trackNameTextView.text = track.title
    }

    private fun setupCards() {
        with(viewBinding) {
            if (studyPlan != null) {
                trackTimeToCompleteTextView.text =
                    if (studyPlan!!.hoursToReachTrack != 0) {
                        "~ ${studyPlan!!.hoursToReachTrack} h"
                    } else {
                        "~ ${studyPlan!!.minutesToReachTrack} m"
                    }
            } else {
                trackProgressTimeCardView.visibility = View.GONE
                (trackProgressCompletedGraduateProjectsCardView.layoutParams as ViewGroup.MarginLayoutParams)
                    .setMargins(0, 0, 0, 0)
            }

            trackCompletedTopicsTextView.text = "${trackProgress.completedTopics} / ${track.topicsCount}"
            trackCompletedTopicsProgressIndicator.progress =
                if (track.topicsCount == 0)
                    0
                else
                    trackProgress.completedTopics * 100 / track.topicsCount

            if (track.capstoneProjects.isEmpty()) {
                trackProgressCompletedGraduateProjectsCardView.visibility = View.GONE
            } else {
                trackCompletedGraduateProjectsTextView.text = "${trackProgress.completedCapstoneProjects.size}"
            }

            if (track.capstoneTopicsCount == 0) {
                trackAppliedCoreTopicsCardView.visibility = View.GONE
            } else {
                trackAppliedCoreTopicsTextView.text = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}"
                trackAppliedCoreTopicsProgressIndicator.progress = trackProgress.appliedCapstoneTopicsCount * 100 / track.capstoneTopicsCount
            }
        }
    }

    private fun setupAboutSection() {
        with(viewBinding) {
            trackAboutUsefulnessTextView.text = "${trackProgress.averageRating}"
            val hoursToComplete = (track.secondsToComplete / 3600).roundToInt()
            trackAboutAllPerformTimeTextView.text = resources.getQuantityString(
                R.plurals.hours,
                hoursToComplete,
                hoursToComplete
            )

            if (track.projects.isEmpty()) {
                trackAboutProjectsCountTextView.visibility = View.GONE
            } else {
                trackAboutProjectsCountTextView.text = resources.getQuantityString(
                    R.plurals.projects,
                    track.projects.size,
                    track.projects.size
                )
            }

            if (track.topicsCount == 0) {
                trackAboutTopicsCountTextView.visibility = View.GONE
            } else {
                trackAboutTopicsCountTextView.text = resources.getQuantityString(
                    R.plurals.topics,
                    track.topicsCount,
                    track.topicsCount
                )
            }

            trackAboutDescriptionTextView.text = track.description

            trackAboutKeepYourProgressInWebTextView.paintFlags = trackAboutKeepYourProgressInWebTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            trackAboutKeepYourProgressInWebTextView.setOnClickListener {
                with(trackViewModel) {
                    onNewMessage(TrackFeature.Message.ClickedContinueInWebEventMessage)
                    onNewMessage(TrackFeature.Message.ClickedContinueInWeb)
                }
            }
        }
    }
}