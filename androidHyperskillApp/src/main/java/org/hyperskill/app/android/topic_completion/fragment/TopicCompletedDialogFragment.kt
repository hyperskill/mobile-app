package org.hyperskill.app.android.topic_completion.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.view.SurfaceHolder
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
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentTopicCompletedBinding
import org.hyperskill.app.android.topic_completion.ui.TopicCompleted
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalViewModel

class TopicCompletedDialogFragment : DialogFragment(R.layout.fragment_topic_completed), SurfaceHolder.Callback {

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

    private var mediaPlayer: MediaPlayer? = null

    private var backgroundAnimationStyle: ViewState.BackgroundAnimationStyle =
        ViewState.BackgroundAnimationStyle.FIRST
    private val isVideoBackgroundPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
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

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
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
        mediaPlayer = MediaPlayer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topicCompletedSurfaceView.holder.addCallback(this@TopicCompletedDialogFragment)
        with(viewBinding.topicCompletedComposeView) {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    val isPlayingState by isVideoBackgroundPlaying.collectAsStateWithLifecycle()
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

    override fun onStop() {
        super.onStop()
        mediaPlayer = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            getBackgroundResource(
                topicCompletedModalViewModel.state.value.backgroundAnimationStyle
            ).use(::setDataSource)
            setDisplay(holder)
            prepareAsync()
            isLooping = true
            setOnPreparedListener {
                it.start()
                isVideoBackgroundPlaying.value = true
            }
        }
    }

    private fun getBackgroundResource(
        backgroundAnimationStyle: ViewState.BackgroundAnimationStyle
    ): AssetFileDescriptor =
        resources
            .openRawResourceFd(
                when (backgroundAnimationStyle) {
                    ViewState.BackgroundAnimationStyle.FIRST -> R.raw.topic_completion_bg_1
                    ViewState.BackgroundAnimationStyle.SECOND -> R.raw.topic_completion_bg_2
                }
            )

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            stop()
            isVideoBackgroundPlaying.value = false
            release()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // no op
    }

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

    interface Callback {
        fun navigateToNextTopic()
        fun navigateToStudyPlan()
    }
}