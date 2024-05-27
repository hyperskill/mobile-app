package org.hyperskill.app.android.topic_completion.fragment

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentTopicCompletedBinding
import org.hyperskill.app.android.topic_completion.model.TopicCompletedModalViewState
import org.hyperskill.app.android.topic_completion.ui.TopicCompleted

class TopicCompletedDialogFragment : DialogFragment(R.layout.fragment_topic_completed), SurfaceHolder.Callback {

    companion object {
        const val TAG = "TopicCompletedFragment"

        fun newInstance(): TopicCompletedDialogFragment =
            TopicCompletedDialogFragment()
    }

    private val viewBinding: FragmentTopicCompletedBinding by viewBinding(FragmentTopicCompletedBinding::bind)

    private var mediaPlayer: MediaPlayer? = null

    private val isVideoBackgroundPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
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
                        TopicCompleted(
                            viewState = TopicCompletedModalViewState(),
                            onCloseClick = ::onCloseClick,
                            onCTAButtonClick = ::onCTAButtonClick
                        )
                    }
                }
            }
        }
    }

    private fun onCloseClick() {
        dialog?.dismiss()
    }

    private fun onCTAButtonClick() {
        TODO("Not implemented yet")
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer = null
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            resources
                .openRawResourceFd(R.raw.topic_completion_bg_1)
                .use(::setDataSource)
            setDisplay(holder)
            prepareAsync()
            isLooping = true
            setOnPreparedListener {
                it.start()
                isVideoBackgroundPlaying.value = true
            }
        }
    }

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
}
