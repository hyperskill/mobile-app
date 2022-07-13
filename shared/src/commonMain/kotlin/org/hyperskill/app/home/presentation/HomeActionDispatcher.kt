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

                val problemOfDayState: HomeFeature.ProblemOfDayState =
                    if (currentProfile.dailyStep == null) {
                        HomeFeature.ProblemOfDayState.Empty
                    } else {
                        val step = stepInteractor.getStep(currentProfile.dailyStep)
                            .getOrElse {
                                onNewMessage(Message.HomeFailure)
                                return
                            }

                        if (step.isCompleted) {
                            HomeFeature.ProblemOfDayState.Solved(step)
                        } else {
                            HomeFeature.ProblemOfDayState.NeedToSolve(step)
                        }
                    }

                val streak = streakInteractor
                    .getStreaks(currentProfile.id)
                    .getOrElse {
                        onNewMessage(Message.HomeFailure)
                        return
                    }
                    .firstOrNull()

                onNewMessage(Message.HomeSuccess(streak, problemOfDayState))
            }
        }
    }
}