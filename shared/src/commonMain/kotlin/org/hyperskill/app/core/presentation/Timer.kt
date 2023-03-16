package org.hyperskill.app.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

// TODO: add timer strategies to use it on Home Screen
class Timer(
    private var duration: Duration,
    private val onChange: (Duration) -> Unit,
    private val onFinish: () -> Unit,
    private val launchIn: CoroutineScope
) {
    companion object {
        private val DURATION_ONE_HOUR = 1.toDuration(DurationUnit.HOURS)
        private val DURATION_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)
    }
    private var isStopped: Boolean = false

    suspend fun run() {
        flow {
            // skip duration to round hour, for example if duration is 03:25:34
            // we will skip here 25 minutes 34 seconds
            if (duration > DURATION_ONE_HOUR) {
                val beforeRoundHour = duration - duration.inWholeHours.toDuration(DurationUnit.HOURS)
                delay(beforeRoundHour)
                duration -= beforeRoundHour
                if (isStopped) return@flow
                emit(duration)
            }

            while (duration.isPositive()) {
                duration -= if (duration > DURATION_ONE_HOUR) {
                    delay(DURATION_ONE_HOUR)
                    DURATION_ONE_HOUR
                } else {
                    delay(DURATION_ONE_MINUTE)
                    DURATION_ONE_MINUTE
                }
                if (isStopped) return@flow
                emit(duration)
            }

            onFinish()
        }
            .onEach {
                onChange(it)
            }
            .launchIn(launchIn)
    }

    fun stop() {
        isStopped = true
    }
}