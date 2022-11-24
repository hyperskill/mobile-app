package org.hyperskill.app.home.presentation

import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class HomeActionDispatcher(
    config: ActionDispatcherOptions,
    private val homeInteractor: HomeInteractor,
    private val streakInteractor: StreakInteractor,
    private val profileInteractor: ProfileInteractor,
    private val stepInteractor: StepInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val topicRepeatedSharedFlow: SharedFlow<Unit>
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    companion object {
        private val DELAY_ONE_MINUTE = 1.toDuration(DurationUnit.MINUTES)

        fun calculateNextProblemIn(): Long {
            val tzNewYork = TimeZone.of("America/New_York")
            val nowInNewYork = Clock.System.now().toLocalDateTime(tzNewYork).toInstant(tzNewYork)
            val tomorrowInNewYork = nowInNewYork.plus(1, DateTimeUnit.DAY, tzNewYork).toLocalDateTime(tzNewYork)
            val startOfTomorrow =
                LocalDateTime(tomorrowInNewYork.year, tomorrowInNewYork.month, tomorrowInNewYork.dayOfMonth, 0, 0, 0, 0)
            return (startOfTomorrow.toInstant(tzNewYork) - nowInNewYork).inWholeSeconds
        }
    }

    init {
        actionScope.launch {
            val currentProfile = profileInteractor
                .getCurrentProfile()
                .getOrElse {
                    onNewMessage(Message.HomeFailure)
                    return@launch
                }
            homeInteractor.solvedStepsSharedFlow.collect { id ->
                if (id == currentProfile.dailyStep) {
                    onNewMessage(Message.ProblemOfDaySolved(id))
                }
            }
        }

        actionScope.launch {
            topicRepeatedSharedFlow.collect {
                onNewMessage(Message.TopicRepeated)
            }
        }
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchHomeScreenData -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile(DataSourceType.REMOTE) // ALTAPPS-303: Get from remote to get relevant problem of the day
                    .getOrElse {
                        onNewMessage(Message.HomeFailure)
                        return
                    }

                val problemOfDayStateResult = actionScope.async {
                    getProblemOfDayState(currentProfile.dailyStep)
                }
                val currentProfileStreaksResult = actionScope.async {
                    streakInteractor.getStreaks(currentProfile.id)
                }

                val problemOfDayState = problemOfDayStateResult.await().getOrElse {
                    onNewMessage(Message.HomeFailure)
                    return
                }
                val currentProfileStreaks = currentProfileStreaksResult.await().getOrElse {
                    onNewMessage(Message.HomeFailure)
                    return
                }

                onNewMessage(
                    Message.HomeSuccess(
                        streak = currentProfileStreaks.firstOrNull(),
                        problemOfDayState = problemOfDayState,
                        recommendedRepetitionsCount = currentProfile.recommendedRepetitionsCount
                    )
                )
                onNewMessage(Message.ReadyToLaunchNextProblemInTimer)
            }
            is Action.LaunchTimer -> {
                flow {
                    var nextProblemIn = calculateNextProblemIn()

                    while (nextProblemIn > 0) {
                        delay(DELAY_ONE_MINUTE)
                        nextProblemIn -= DELAY_ONE_MINUTE.inWholeSeconds
                        emit(nextProblemIn)
                    }
                }
                    .onEach { seconds -> onNewMessage(Message.HomeNextProblemInUpdate(seconds)) }
                    .launchIn(actionScope)
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }

    private suspend fun getProblemOfDayState(dailyStepId: Long?): Result<HomeFeature.ProblemOfDayState> =
        if (dailyStepId == null) {
            Result.success(HomeFeature.ProblemOfDayState.Empty)
        } else {
            val nextProblemIn = calculateNextProblemIn()
            stepInteractor
                .getStep(dailyStepId)
                .map { step ->
                    if (step.isCompleted) {
                        HomeFeature.ProblemOfDayState.Solved(step, nextProblemIn)
                    } else {
                        HomeFeature.ProblemOfDayState.NeedToSolve(step, nextProblemIn)
                    }
                }
        }
}