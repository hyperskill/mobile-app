package org.hyperskill.app.home.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.home.presentation.HomeFeature.Action
import org.hyperskill.app.home.presentation.HomeFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class HomeActionDispatcher(
    config: ActionDispatcherOptions,
    private val streakInteractor: StreakInteractor,
    private val profileInteractor: ProfileInteractor,
    private val stepInteractor: StepInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchHomeScreenData -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse {
                        onNewMessage(Message.HomeFailure)
                        return
                    }

                val problemOfDayState = getProblemOfDayState(currentProfile.dailyStep)
                    .getOrElse {
                        onNewMessage(Message.HomeFailure)
                        return
                    }

                val message = streakInteractor
                    .getStreaks(currentProfile.id)
                    .map { Message.HomeSuccess(it.firstOrNull(), problemOfDayState) }
                    .getOrElse { Message.HomeFailure }

                onNewMessage(message)
            }
        }
    }

    private suspend fun getProblemOfDayState(dailyStepId: Long?): Result<HomeFeature.ProblemOfDayState> =
        if (dailyStepId == null) {
            Result.success(HomeFeature.ProblemOfDayState.Empty)
        } else {
            stepInteractor
                .getStep(dailyStepId)
                .map { step ->
                    if (step.isCompleted) {
                        HomeFeature.ProblemOfDayState.Solved(step)
                    } else {
                        HomeFeature.ProblemOfDayState.NeedToSolve(step)
                    }
                }
        }
}