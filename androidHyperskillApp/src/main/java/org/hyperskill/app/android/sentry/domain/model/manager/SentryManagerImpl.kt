package org.hyperskill.app.android.sentry.domain.model.manager

import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.SpanStatus
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import io.sentry.protocol.User
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.sentry.domain.model.transaction.PlatformHyperskillSentryTransaction
import org.hyperskill.app.android.sentry.extensions.Breadcrumb
import org.hyperskill.app.android.sentry.extensions.toSentryLevel
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction

class SentryManagerImpl : SentryManager {
    private val currentTransactionsMap = mutableMapOf<Int, PlatformHyperskillSentryTransaction>()

    override fun setup() {
        SentryAndroid.init(HyperskillApp.application) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = "${BuildKonfig.FLAVOR}-${BuildConfig.BUILD_TYPE}"
            options.release = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            options.isEnableAutoSessionTracking = true
            options.isAnrEnabled = true
            options.addIntegration(
                FragmentLifecycleIntegration(
                    HyperskillApp.application,
                    enableFragmentLifecycleBreadcrumbs = true,
                    enableAutoFragmentLifecycleTracing = true
                )
            )

            if (BuildConfig.DEBUG) {
                options.setDebug(true)
                options.setDiagnosticLevel(SentryLevel.WARNING)
                options.tracesSampleRate = 1.0
            } else {
                options.setDebug(false)
                options.tracesSampleRate = 0.3
            }
        }
    }

    override fun addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) {
        val sentryBreadcrumb = Breadcrumb(hyperskillSentryBreadcrumb = breadcrumb)
        Sentry.addBreadcrumb(sentryBreadcrumb)
    }

    override fun captureMessage(message: String, level: HyperskillSentryLevel) {
        Sentry.captureMessage(message, level.toSentryLevel())
    }

    override fun setUsedId(userId: String) {
        val user = User().apply {
            id = userId
        }
        Sentry.setUser(user)
    }

    override fun clearCurrentUser() {
        Sentry.configureScope { scope ->
            scope.user = null
        }
    }

    override fun containsOngoingTransaction(transaction: HyperskillSentryTransaction): Boolean =
        currentTransactionsMap.containsKey(mapTransactionInfoToKey(transaction))

    override fun startTransaction(transaction: HyperskillSentryTransaction) {
        val sentryTransaction = Sentry.startTransaction(transaction.name, transaction.operation)
        val platformTransaction = PlatformHyperskillSentryTransaction(sentryTransaction)
        currentTransactionsMap[mapTransactionInfoToKey(platformTransaction)] = platformTransaction
    }

    override fun finishTransaction(transaction: HyperskillSentryTransaction, throwable: Throwable?) {
        val platformTransaction = currentTransactionsMap[mapTransactionInfoToKey(transaction)] ?: return

        if (throwable != null) {
            platformTransaction.transaction.throwable = throwable
            platformTransaction.transaction.status = SpanStatus.UNKNOWN_ERROR
        } else {
            platformTransaction.transaction.status = SpanStatus.OK
        }

        platformTransaction.transaction.finish()
        currentTransactionsMap.remove(mapTransactionInfoToKey(platformTransaction))
    }

    private fun mapTransactionInfoToKey(transactionInfo: HyperskillSentryTransaction): Int =
        transactionInfo.hashCode()
}