package org.hyperskill.app.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

// TODO: add timer strategies to use it on Home Screen
class Timer(
    private val duration: Duration,
    private val onChange: (Duration) -> Unit,
    private val onFinish: suspend () -> Unit,
    private val launchIn: CoroutineScope
) {
    companion object {
        private val DURATION_ONE_HOUR = 1.toDuration(DurationUnit.HOURS)
        private val DURATION_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)
    }

    private var timerJob: Job? = null

    fun start() {
        timerJob?.cancel()
        var actualDuration = duration
        timerJob = flow {
            // skip duration to round hour, for example if duration is 03:25:34
            // we will skip here 25 minutes 34 seconds
            if (actualDuration >= DURATION_ONE_HOUR) {
                val beforeRoundHour = actualDuration - actualDuration.inWholeHours.toDuration(DurationUnit.HOURS)
                delay(beforeRoundHour)
                actualDuration -= beforeRoundHour
                emit(actualDuration)
            }
            // while duration is positive
            // tick every hour if duration is more than one hour
            // or tick every minute if duration if less than one hour
            while (actualDuration.isPositive()) {
                actualDuration -= if (actualDuration > DURATION_ONE_HOUR) {
                    delay(DURATION_ONE_HOUR)
                    DURATION_ONE_HOUR
                } else {
                    delay(DURATION_ONE_MINUTE)
                    DURATION_ONE_MINUTE
                }
                emit(actualDuration)
            }

            onFinish()
        }
            .cancellable()
            .onCompletion {
                timerJob = null
            }
            .onEach {
                onChange(it)
            }
            .launchIn(launchIn)
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
    }
}