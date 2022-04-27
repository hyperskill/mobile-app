package org.hyperskill.app.android.core.view.base

import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import io.sentry.protocol.User
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.remote.UserAgentInfo
import javax.inject.Inject

class SentryManager
@Inject
constructor(
    private val userAgentInfo: UserAgentInfo
) {
    fun configure(application: HyperskillApp) {
        SentryAndroid.init(application) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = "${BuildKonfig.FLAVOR}-${BuildConfig.BUILD_TYPE}"
            options.release = "${userAgentInfo.versionName} (${userAgentInfo.versionCode})"
            options.isEnableAutoSessionTracking = true
            options.isAnrEnabled = true
            options.addIntegration(
                FragmentLifecycleIntegration(
                    application,
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

    fun updateUserID(userID: String) {
        val user = User().apply {
            id = userID
        }
        Sentry.setUser(user)
    }

    fun clearUser() {
        Sentry.setUser(null)
    }
}