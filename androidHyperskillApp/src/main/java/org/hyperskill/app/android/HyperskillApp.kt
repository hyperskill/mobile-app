package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import org.hyperskill.app.android.core.view.base.SentryManager
import org.hyperskill.app.android.core.injection.AppCoreComponent
import org.hyperskill.app.android.core.injection.DaggerAppCoreComponent
import ru.nobird.android.view.base.ui.extension.isMainProcess
import javax.inject.Inject

class HyperskillApp : Application() {
    companion object {
        lateinit var application: HyperskillApp

        fun component(): AppCoreComponent =
            application.component

        fun getAppContext(): Context =
            application.applicationContext
    }

    @Inject
    internal lateinit var sentryManager: SentryManager

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
        sentryManager.configure(this)
    }
}