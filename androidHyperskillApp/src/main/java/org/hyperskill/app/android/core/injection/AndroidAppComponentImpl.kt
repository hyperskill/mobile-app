package org.hyperskill.app.android.core.injection

import android.app.Application
import android.content.Context
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponentImpl
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponentImpl
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponentImpl
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.main.injection.NavigationComponentImpl
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponentImpl
import org.hyperskill.app.android.sentry.domain.model.manager.SentryManagerImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponentImpl
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.CommonAndroidAppGraphImpl
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.core.injection.CommonComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl

class AndroidAppComponentImpl(
    private val application: Application,
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant
) : AndroidAppComponent, CommonAndroidAppGraphImpl() {
    override val context: Context
        get() = application

    override val commonComponent: CommonComponent by lazy {
        CommonComponentImpl(application, buildVariant, userAgentInfo)
    }

    override val imageLoadingComponent: ImageLoadingComponent by lazy {
        ImageLoadingComponentImpl(context)
    }

    override val navigationComponent: NavigationComponent by lazy {
        NavigationComponentImpl()
    }

    override val sentryComponent: SentryComponent by lazy {
        SentryComponentImpl(SentryManagerImpl(commonComponent.buildKonfig))
    }

    override val platformNotificationComponent: PlatformNotificationComponent by lazy {
        PlatformNotificationComponentImpl(application, this)
    }

    override fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent =
        PlatformAuthSocialWebViewComponentImpl()

    /**
     * Latex component
     */
    override fun buildPlatformLatexComponent(): PlatformLatexComponent =
        PlatformLatexComponentImpl(application, networkComponent.endpointConfigInfo)

    /**
     * CodeEditor component
     */
    override fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent =
        PlatformCodeEditorComponentImpl(application)
}