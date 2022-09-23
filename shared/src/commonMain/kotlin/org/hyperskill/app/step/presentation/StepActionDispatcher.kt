package org.hyperskill.app.step.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.step.domain.analytic.StepClickedBackHyperskillAnalyticEvent
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.presentation.StepFeature.Action
import org.hyperskill.app.step.presentation.StepFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepInteractor: StepInteractor,
    private val profileInteractor: ProfileInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchStep -> {
                val result = stepInteractor.getStep(action.stepId)

                val message =
                    result
                        .map { Message.StepLoaded.Success(it) }
                        .getOrElse {
                            Message.StepLoaded.Error(errorMsg = it.message ?: "")
                        }

                onNewMessage(message)
            }
            is Action.LogClickedBackEvent -> {
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse { return }

                val analyticEvent = StepClickedBackHyperskillAnalyticEvent(
                    if (action.stepId == currentProfile.dailyStep)
                        HyperskillAnalyticRoute.Learn.Daily(action.stepId)
                    else HyperskillAnalyticRoute.Learn.Step(action.stepId)
                )
                analyticInteractor.logEvent(analyticEvent)
            }
        }
    }
}