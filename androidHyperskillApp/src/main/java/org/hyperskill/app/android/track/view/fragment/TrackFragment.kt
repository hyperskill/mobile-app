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
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.launchUrl
import org.hyperskill.app.android.core.view.ui.dialog.CreateMagicLinkLoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.databinding.FragmentTrackBinding
import org.hyperskill.app.track.domain.model.Track
import org.hyperskill.app.track.presentation.TrackFeature
import org.hyperskill.app.track.presentation.TrackViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
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
            is TrackFeature.Action.ViewAction.OpenUrl ->
                requireContext().launchUrl(action.url)
            TrackFeature.Action.ViewAction.ShowGetMagicLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
        }
    }

    override fun render(state: TrackFeature.State) {
        viewStateDelegate.switchState(state)

        if (state is TrackFeature.State.Content) {
            renderContent(state)
        }
    }

    private fun renderContent(content: TrackFeature.State.Content) {
        if (content.isLoadingMagicLink) {
            CreateMagicLinkLoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, CreateMagicLinkLoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager.dismissDialogFragmentIfExists(CreateMagicLinkLoadingProgressDialogFragment.TAG)
        }
        renderTrackCoverAndName(content.track)
        renderCards(content)
        renderAboutSection(content)
    }

    private fun renderTrackCoverAndName(track: Track) {
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
                if (content.track.topicsCount == 0)
                    0
                else
                    content.trackProgress.completedTopics * 100 / content.track.topicsCount

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
                R.plurals.hours,
                hoursToComplete,
                hoursToComplete
            )

            if (content.track.projects.isEmpty()) {
                trackAboutProjectsCountTextView.visibility = View.GONE
            } else {
                trackAboutProjectsCountTextView.text = resources.getQuantityString(
                    R.plurals.projects,
                    content.track.projects.size,
                    content.track.projects.size
                )
            }

            if (content.track.topicsCount == 0) {
                trackAboutTopicsCountTextView.visibility = View.GONE
            } else {
                trackAboutTopicsCountTextView.text = resources.getQuantityString(
                    R.plurals.topics,
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
}