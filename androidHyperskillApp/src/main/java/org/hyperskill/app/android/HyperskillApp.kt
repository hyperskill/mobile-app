package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import org.hyperskill.app.android.core.injection.AppCoreComponent
import org.hyperskill.app.android.core.injection.DaggerAppCoreComponent
import org.hyperskill.app.config.BuildKonfig
import ru.nobird.android.view.base.ui.extension.isMainProcess

class HyperskillApp : Application() {
    companion object {
        lateinit var application: HyperskillApp

        fun component(): AppCoreComponent =
            application.component

        fun getAppContext(): Context =
            application.applicationContext
    }

    private lateinit var component: AppCoreComponent

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess) return

        setTheme(R.style.AppTheme)

        application = this

        component = DaggerAppCoreComponent.builder()
            .context(application)
            .build()

        component.inject(this)
        initSentry()
    }

    private fun initSentry() {
        SentryAndroid.init(application) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = "${BuildKonfig.FLAVOR}-${BuildConfig.BUILD_TYPE}"
            options.release = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
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
}