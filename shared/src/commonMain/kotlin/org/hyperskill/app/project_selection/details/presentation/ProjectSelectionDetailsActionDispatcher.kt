package org.hyperskill.app.project_selection.details.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.project_selection.details.domain.interactor.ProjectSelectionDetailsInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class ProjectSelectionDetailsActionDispatcher(
    config: ActionDispatcherOptions,
    private val projectSelectionDetailsInteractor: ProjectSelectionDetailsInteractor,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<ProjectSelectionDetailsFeature.Action, ProjectSelectionDetailsFeature.Message>(
    config.createConfig()
) {
    override suspend fun doSuspendableAction(action: ProjectSelectionDetailsFeature.Action) {
        when (action) {
            is ProjectSelectionDetailsFeature.InternalAction.FetchContent -> {
                val sentryTransaction =
                    HyperskillSentryTransactionBuilder.buildProjectSelectionDetailsScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                projectSelectionDetailsInteractor.getContentData(
                    trackId = action.trackId,
                    projectId = action.projectId,
                    forceLoadFromNetwork = action.forceLoadFromNetwork
                ).fold(
                    onSuccess = { contentData ->
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(ProjectSelectionDetailsFeature.ContentFetchResult.Success(contentData))
                    },
                    onFailure = { throwable ->
                        sentryInteractor.finishTransaction(sentryTransaction, throwable)
                        onNewMessage(ProjectSelectionDetailsFeature.ContentFetchResult.Error)
                    }
                )
            }
            is ProjectSelectionDetailsFeature.InternalAction.SelectProject -> {
                projectSelectionDetailsInteractor.selectProject(
                    trackId = action.trackId,
                    projectId = action.projectId
                ).fold(
                    onSuccess = {
                        onNewMessage(ProjectSelectionDetailsFeature.ProjectSelectionResult.Success)
                    },
                    onFailure = {
                        onNewMessage(ProjectSelectionDetailsFeature.ProjectSelectionResult.Error)
                    }
                )
            }
            ProjectSelectionDetailsFeature.InternalAction.ClearProjectsCache -> {
                projectSelectionDetailsInteractor.clearProjectsCache()
            }
            is ProjectSelectionDetailsFeature.InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }
}