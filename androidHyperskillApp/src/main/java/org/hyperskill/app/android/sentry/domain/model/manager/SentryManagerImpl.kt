package org.hyperskill.app.android.sentry.domain.model.manager

import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.sentry.extensions.Breadcrumb
import org.hyperskill.app.android.sentry.extensions.toSentryLevel
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel
import org.hyperskill.app.sentry.domain.model.manager.SentryManager

class SentryManagerImpl : SentryManager {
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
}