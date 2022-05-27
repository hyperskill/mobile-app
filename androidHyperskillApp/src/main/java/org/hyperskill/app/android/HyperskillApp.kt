package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import android.os.Build
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import org.hyperskill.app.android.core.injection.AppCoreComponent
import org.hyperskill.app.android.core.injection.DaggerAppCoreComponent
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.injection.AndroidAppComponent
import org.hyperskill.app.core.injection.AppGraphImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import ru.nobird.android.view.base.ui.extension.isMainProcess

class HyperskillApp : Application() {
    companion object {
        lateinit var application: HyperskillApp

        fun component(): AppCoreComponent =
            application.component

        fun getAppContext(): Context =
            application.applicationContext

        fun graph(): AndroidAppComponent =
            application.appGraph
    }

    private lateinit var appGraph: AndroidAppComponent

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
        appGraph = AppGraphImpl(this, buildUserAgentInfo())
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

    private fun buildUserAgentInfo() =
        UserAgentInfo(
            BuildConfig.VERSION_NAME,
            "Android ${Build.VERSION.SDK_INT}",
            BuildConfig.VERSION_CODE.toString(),
            BuildConfig.APPLICATION_ID
        )
}