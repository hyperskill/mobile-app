package org.hyperskill.app.main.presentation

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main.presentation.AppFeature.Action
import org.hyperskill.app.main.presentation.AppFeature.Message
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class AppActionDispatcher(
    config: ActionDispatcherOptions,
    private val appInteractor: AppInteractor,
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    init {
        authInteractor
            .observeUserDeauthorization()
            .onEach {
                when (it.reason) {
                    UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE -> {
                        authInteractor.clearCache()
                    }
                    UserDeauthorized.Reason.SIGN_OUT -> {
                        appInteractor.doCurrentUserSignedOutCleanUp()
                    }
                }

                sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppUserDeauthorized(it.reason))

                onNewMessage(Message.UserDeauthorized(it.reason))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.DetermineUserAccountStatus -> {
                val transaction = HyperskillSentryTransactionBuilder.buildAppScreenRemoteDataLoading()
                sentryInteractor.startTransaction(transaction)

                sentryInteractor.addBreadcrumb(HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatus())

                profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                    .fold(
                        onSuccess = { profile ->
                            sentryInteractor.addBreadcrumb(
                                HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusSuccess()
                            )
                            sentryInteractor.finishTransaction(transaction)
                            onNewMessage(Message.UserAccountStatus(profile))
                        },
                        onFailure = { exception ->
                            sentryInteractor.addBreadcrumb(
                                HyperskillSentryBreadcrumbBuilder.buildAppDetermineUserAccountStatusError(exception)
                            )
                            sentryInteractor.finishTransaction(transaction, exception)
                            onNewMessage(Message.UserAccountStatusError)
                        }
                    )
            }
            is Action.IdentifyUserInSentry ->
                sentryInteractor.setUsedId(action.userId)
            is Action.ClearUserInSentry ->
                sentryInteractor.clearCurrentUser()
            else -> {}
        }
    }
}