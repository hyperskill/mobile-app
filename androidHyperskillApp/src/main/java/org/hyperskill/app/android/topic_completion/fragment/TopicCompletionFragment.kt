package org.hyperskill.app.android.topic_completion.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentTopicCompletionBinding

class TopicCompletionFragment : Fragment(R.layout.fragment_topic_completion), SurfaceHolder.Callback {

    companion object {
        fun newInstance(): TopicCompletionFragment =
            TopicCompletionFragment()
    }

    private val viewBinding: FragmentTopicCompletionBinding by viewBinding(FragmentTopicCompletionBinding::bind)

    private var mediaPlayer: MediaPlayer? = null

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding.topicCompletionSurfaceView) {
            holder.addCallback(this@TopicCompletionFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
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
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // no op
    }
}
