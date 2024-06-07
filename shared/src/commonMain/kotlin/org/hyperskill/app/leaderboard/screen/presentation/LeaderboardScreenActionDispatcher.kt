package org.hyperskill.app.leaderboard.screen.presentation

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Action
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.InternalAction
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.InternalMessage
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class LeaderboardScreenActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    private var isDailyLeaderboardTimerLaunched: Boolean = false
    private var isWeeklyLeaderboardTimerLaunched: Boolean = false

    companion object {
        private val TIMER_TICK_INTERVAL = 1.toDuration(DurationUnit.MINUTES)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LaunchDailyLeaderboardTimer -> {
                handleLaunchDailyLeaderboardTimerAction(action, ::onNewMessage)
            }
            is InternalAction.LaunchWeeklyLeaderboardTimer -> {
                handleLaunchWeeklyLeaderboardTimerAction(action, ::onNewMessage)
            }
            else -> {
                // no op
            }
        }
    }

    private fun handleLaunchDailyLeaderboardTimerAction(
        action: InternalAction.LaunchDailyLeaderboardTimer,
        onNewMessage: (Message) -> Unit
    ) {
        if (isDailyLeaderboardTimerLaunched) {
            return
        }

        isDailyLeaderboardTimerLaunched = true

        flow {
            var secondsUntilStartOfNextDay = action.secondsUntilStartOfNextDay

            while (secondsUntilStartOfNextDay > 0) {
                delay(TIMER_TICK_INTERVAL)
                secondsUntilStartOfNextDay -= TIMER_TICK_INTERVAL.inWholeSeconds
                emit(secondsUntilStartOfNextDay)
            }
        }
            .onCompletion {
                isDailyLeaderboardTimerLaunched = false
                onNewMessage(InternalMessage.DailyLeaderboardTimerCompleted)
            }
            .onEach { seconds ->
                onNewMessage(InternalMessage.DailyLeaderboardTimerTick(seconds))
            }
            .launchIn(actionScope)
    }

    private fun handleLaunchWeeklyLeaderboardTimerAction(
        action: InternalAction.LaunchWeeklyLeaderboardTimer,
        onNewMessage: (Message) -> Unit
    ) {
        if (isWeeklyLeaderboardTimerLaunched) {
            return
        }

        isWeeklyLeaderboardTimerLaunched = true

        flow {
            var secondsUntilNextSunday = action.secondsUntilNextSunday

            while (secondsUntilNextSunday > 0) {
                delay(TIMER_TICK_INTERVAL)
                secondsUntilNextSunday -= TIMER_TICK_INTERVAL.inWholeSeconds
                emit(secondsUntilNextSunday)
            }
        }
            .onCompletion {
                isWeeklyLeaderboardTimerLaunched = false
                onNewMessage(InternalMessage.WeeklyLeaderboardTimerCompleted)
            }
            .onEach { seconds ->
                onNewMessage(InternalMessage.WeeklyLeaderboardTimerTick(seconds))
            }
            .launchIn(actionScope)
    }
}