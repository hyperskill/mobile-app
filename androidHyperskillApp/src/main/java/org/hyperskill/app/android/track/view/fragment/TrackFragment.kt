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
            addState<TrackFeature.State.Loading>(viewBinding.trackProgress)
            addState<TrackFeature.State.NetworkError>(viewBinding.trackError.root)
            addState<TrackFeature.State.Content>(viewBinding.trackContainer)
        }
    }

    override fun onAction(action: TrackFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: TrackFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is TrackFeature.State.Loading) {
            viewBinding.trackProgress.visibility = View.VISIBLE
        }
        if (state is TrackFeature.State.Content) {
            initTrack(state.track, state.trackProgress)
        }
    }

    private fun initTrack(track: Track, trackProgress: TrackProgress) {
        val svgImageLoader = ImageLoader.Builder(requireContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        viewBinding.trackIconImageView.load(track.cover, svgImageLoader) {
            scale(Scale.FILL)
        }
        viewBinding.trackNameTextView.text = track.title

        initCards(track, trackProgress)
        initAboutSection(track, trackProgress)
    }

    private fun initCards(track: Track, trackProgress: TrackProgress) {
        with(viewBinding) {
            // TODO must get this time from /api/study-plans
            trackTimeToCompleteTextView.text = "~ ${(track.secondsToComplete / 3600).roundToInt()} h"

            trackCompletedGraduateProjectsTextView.text = "${trackProgress.completedCapstoneProjects.size}"

            trackCompletedTopicsTextView.text = "${trackProgress.learnedTopicsCount} / ${track.topicsCount}"
            trackCompletedTopicsProgressIndicator.progress = trackProgress.learnedTopicsCount / track.topicsCount * 100

            trackAppliedCoreTopicsTextView.text = "${trackProgress.appliedCapstoneTopicsCount} / ${track.capstoneTopicsCount}"
            trackAppliedCoreTopicsProgressIndicator.progress = trackProgress.appliedCapstoneTopicsCount / track.capstoneTopicsCount * 100
        }
    }

    private fun initAboutSection(track: Track, trackProgress: TrackProgress) {
        with(viewBinding) {
            trackAboutTextView.text = "${trackProgress.usefulness ?: "?"} hours"
            trackAboutAllPerformTimeTextView.text = "${(track.secondsToComplete / 3600).roundToInt()} hours"
            trackAboutProjectsCountTextView.text = "${track.projects.size} projects"
            trackAboutTopicsCountTextView.text = "${track.topicsCount} topics"
            trackAboutDescriptionTextView.text = track.description

            // TODO fromHtml is deprecated, what to use instead?
            trackAboutKeepYourProgressInWebTextView.text = Html.fromHtml(resources.getString(R.string.track_keep_progress_in_web_text))
            trackAboutKeepYourProgressInWebTextView.setOnClickListener {
                // TODO where to send user?
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(BuildKonfig.BASE_URL)
                startActivity(intent)
            }
        }
    }
}