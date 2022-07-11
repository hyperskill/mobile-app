package org.hyperskill.app.android.track.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.size.Scale
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentTrackBinding
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.track.domain.model.StudyPlan
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.domain.model.TrackProgress
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import kotlin.math.roundToInt

class TrackFragment :
    Fragment(R.layout.fragment_track),
    ReduxView<TrackFeature.State, TrackFeature.Action.ViewAction> {
    companion object {
        fun newInstance(trackId: Long): Fragment =
            TrackFragment()
                .apply {
                    this.trackId = trackId
                }
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentTrackBinding by viewBinding(FragmentTrackBinding::bind)
    private val trackViewModel: TrackViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<TrackFeature.State> = ViewStateDelegate()

    private var trackId: Long by argument()

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
            trackViewModel.onNewMessage(TrackFeature.Message.Init(trackId, forceUpdate = true))
        }

        trackViewModel.onNewMessage(TrackFeature.Message.Init(trackId))
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
        // no op
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
            // TODO must get this time from /api/study-plans
            val hoursToComplete = studyPlan?.secondsToReachTrack?.toFloat()?.div(3600)?.roundToInt() ?: 0
            trackTimeToCompleteTextView.text = "~ $hoursToComplete h"

            trackCompletedGraduateProjectsTextView.text = "${trackProgress.completedCapstoneProjects.size}"

            trackCompletedTopicsTextView.text = "${trackProgress.learnedTopicsCount} / ${track.topicsCount}"
            trackCompletedTopicsProgressIndicator.progress =
                if (track.topicsCount == 0)
                    0
                else
                    trackProgress.learnedTopicsCount / track.topicsCount * 100

            trackAppliedCoreTopicsTextView.text = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}"
            trackAppliedCoreTopicsProgressIndicator.progress =
                if (track.capstoneTopicsCount == 0)
                    0
                else
                    trackProgress.appliedCapstoneTopicsCount / track.capstoneTopicsCount * 100
        }
    }

    private fun setupAboutSection() {
        with(viewBinding) {
            trackAboutUsefulnessTextView.text = "${trackProgress.usefulness ?: "0"}"
            trackAboutAllPerformTimeTextView.text = "${(track.secondsToComplete / 3600).roundToInt()} hours"
            trackAboutProjectsCountTextView.text = "${track.projects.size} projects"
            trackAboutTopicsCountTextView.text = "${track.topicsCount} topics"
            trackAboutDescriptionTextView.text = track.description

            trackAboutKeepYourProgressInWebTextView.text = Html.fromHtml(resources.getString(R.string.track_continue_in_web_text))
            trackAboutKeepYourProgressInWebTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(BuildKonfig.BASE_URL + "tracks/${track.id}")
                startActivity(intent)
            }
        }
    }
}