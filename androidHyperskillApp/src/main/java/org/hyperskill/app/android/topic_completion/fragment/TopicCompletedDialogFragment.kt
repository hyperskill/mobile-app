package org.hyperskill.app.android.topic_completion.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentTopicCompletedBinding
import org.hyperskill.app.android.topic_completion.delegate.TopicCompletedMediaPlayerDelegate
import org.hyperskill.app.android.topic_completion.ui.TopicCompleted
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalViewModel

class TopicCompletedDialogFragment : DialogFragment(R.layout.fragment_topic_completed) {

    companion object {
        const val TAG = "TopicCompletedFragment"

        fun newInstance(params: TopicCompletedModalFeatureParams): TopicCompletedDialogFragment =
            TopicCompletedDialogFragment().apply {
                this.params = params
            }
    }

    private var params: TopicCompletedModalFeatureParams by argument(TopicCompletedModalFeatureParams.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val topicCompletedModalViewModel: TopicCompletedModalViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private val viewBinding: FragmentTopicCompletedBinding by viewBinding(FragmentTopicCompletedBinding::bind)

    private var mediaPlayerDelegate: TopicCompletedMediaPlayerDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        mediaPlayerDelegate = TopicCompletedMediaPlayerDelegate(
            lifecycle = lifecycle,
            providePlayingResource = ::getBackgroundResource
        )
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformTopicCompletedModalComponent(params)
                .reduxViewModelFactory
        topicCompletedModalViewModel.handleActions(this, ::onAction)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setOnShowListener {
                topicCompletedModalViewModel.onNewMessage(Message.ShownEventMessage)
            }
        }

    override fun onStart() {
        super.onStart()
        dialog
            ?.window
            ?.let { window ->
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                window.setWindowAnimations(R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topicCompletedSurfaceView.holder.addCallback(requireMediaDelegate())
        with(viewBinding.topicCompletedComposeView) {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                val isPlayingState by requireMediaDelegate().isVideoBackgroundPlaying.collectAsStateWithLifecycle()
                HyperskillTheme {
                    if (isPlayingState) {
                        TopicCompleted(topicCompletedModalViewModel)
                    }
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        topicCompletedModalViewModel.onNewMessage(Message.HiddenEventMessage)
        super.onDismiss(dialog)
    }

    override fun onDestroy() {
        mediaPlayerDelegate = null
        super.onDestroy()
    }

    private fun getBackgroundResource(): AssetFileDescriptor =
        resources
            .openRawResourceFd(
                when (topicCompletedModalViewModel.state.value.backgroundAnimationStyle) {
                    ViewState.BackgroundAnimationStyle.FIRST -> R.raw.topic_completion_bg_1
                    ViewState.BackgroundAnimationStyle.SECOND -> R.raw.topic_completion_bg_2
                }
            )

    private fun onAction(action: TopicCompletedModalFeature.Action.ViewAction) {
        when (action) {
            TopicCompletedModalFeature.Action.ViewAction.Dismiss -> {
                dialog?.dismiss()
            }
            TopicCompletedModalFeature.Action.ViewAction.NavigateTo.NextTopic -> {
                (parentFragment as? Callback)?.navigateToNextTopic()
                dismiss()
            }
            TopicCompletedModalFeature.Action.ViewAction.NavigateTo.StudyPlan -> {
                (parentFragment as? Callback)?.navigateToStudyPlan()
                dismiss()
            }
        }
    }

    private fun requireMediaDelegate(): TopicCompletedMediaPlayerDelegate =
        requireNotNull(mediaPlayerDelegate)

    interface Callback {
        fun navigateToNextTopic()
        fun navigateToStudyPlan()
    }
}