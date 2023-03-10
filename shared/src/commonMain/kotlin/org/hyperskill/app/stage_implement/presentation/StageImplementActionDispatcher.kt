package org.hyperskill.app.stage_implement.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StageImplementActionDispatcher(
    config: ActionDispatcherOptions,
    private val stagesInteractor: StagesInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchStageImplement -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStageImplementScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                stagesInteractor.getStage(action.stageId)
                    .fold(
                        onSuccess = { stage ->
                            sentryInteractor.finishTransaction(sentryTransaction)
                            onNewMessage(Message.FetchStageImplementResult.Success(action.projectId, stage))
                        },
                        onFailure = { exception ->
                            sentryInteractor.finishTransaction(sentryTransaction, exception)
                            onNewMessage(Message.FetchStageImplementResult.NetworkError)
                        }
                    )
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}