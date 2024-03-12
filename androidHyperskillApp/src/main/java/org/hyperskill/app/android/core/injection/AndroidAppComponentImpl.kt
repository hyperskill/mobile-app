package org.hyperskill.app.android.core.injection

import android.app.Application
import org.hyperskill.app.analytic.injection.PlatformAnalyticComponent
import org.hyperskill.app.analytic.injection.PlatformAnalyticComponentImpl
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponentImpl
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponentImpl
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponentImpl
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.main.injection.NavigationComponentImpl
import org.hyperskill.app.android.notification.local.injection.PlatformLocalNotificationComponent
import org.hyperskill.app.android.notification.local.injection.PlatformLocalNotificationComponentImpl
import org.hyperskill.app.android.notification.remote.injection.AndroidPlatformPushNotificationComponent
import org.hyperskill.app.android.notification.remote.injection.AndroidPlatformPushNotificationsComponentImpl
import org.hyperskill.app.android.sentry.domain.model.manager.SentryManagerImpl
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.CommonAndroidAppGraphImpl
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.core.injection.CommonComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.main.injection.PlatformMainComponentImpl
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl

class AndroidAppComponentImpl(
    override val application: Application,
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant
) : AndroidAppComponent, CommonAndroidAppGraphImpl() {

    override val commonComponent: CommonComponent by lazy {
        CommonComponentImpl(this.application, buildVariant, userAgentInfo)
    }

    override val imageLoadingComponent: ImageLoadingComponent by lazy {
        ImageLoadingComponentImpl(this.application)
    }

    override val navigationComponent: NavigationComponent by lazy {
        NavigationComponentImpl()
    }

    override val sentryComponent: SentryComponent by lazy {
        SentryComponentImpl(SentryManagerImpl(commonComponent.buildKonfig))
    }

    override val platformLocalNotificationComponent: PlatformLocalNotificationComponent by lazy {
        PlatformLocalNotificationComponentImpl(this.application, this)
    }

    /**
     * Analytic component
     */
    override val platformAnalyticComponent: PlatformAnalyticComponent by lazy {
        PlatformAnalyticComponentImpl(
            loggerComponent = loggerComponent,
            applicationContext = this.application.applicationContext
        )
    }

    /**
     * Main component
     */
    override val platformMainComponent: PlatformMainComponent by lazy {
        PlatformMainComponentImpl(
            mainComponent = mainComponent,
            analyticComponent = analyticComponent
        )
    }

    override fun buildPlatformPushNotificationsComponent(): AndroidPlatformPushNotificationComponent =
        AndroidPlatformPushNotificationsComponentImpl(this)

    /**
     * Latex component
     */
    override fun buildPlatformLatexComponent(): PlatformLatexComponent =
        PlatformLatexComponentImpl(this.application, networkComponent.endpointConfigInfo)

    /**
     * CodeEditor component
     */
    override fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent =
        PlatformCodeEditorComponentImpl(this.application)
}