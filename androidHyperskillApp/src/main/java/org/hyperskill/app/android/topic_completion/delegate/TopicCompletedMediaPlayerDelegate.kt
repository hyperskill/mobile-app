package org.hyperskill.app.android.topic_completion.delegate

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.view.SurfaceHolder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopicCompletedMediaPlayerDelegate(
    lifecycle: Lifecycle,
    private val providePlayingResource: () -> AssetFileDescriptor
) : SurfaceHolder.Callback {

    private var mediaPlayer: MediaPlayer? = null

    init {
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        mediaPlayer = MediaPlayer()
                    }
                    Lifecycle.Event.ON_STOP -> {
                        mediaPlayer = null
                    }
                    else -> {
                        // no op
                    }
                }
            }
        )
    }

    private val _isVideoBackgroundPlaying: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVideoBackgroundPlaying: StateFlow<Boolean>
        get() = _isVideoBackgroundPlaying

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            providePlayingResource().use(::setDataSource)
            setDisplay(holder)
            prepareAsync()
            isLooping = true
            setOnPreparedListener {
                it.start()
                _isVideoBackgroundPlaying.value = true
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mediaPlayer?.apply {
            // Don't set _isVideoBackgroundPlaying.value = false, to not restart enter animation
            stop()
            release()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // no op
    }
}