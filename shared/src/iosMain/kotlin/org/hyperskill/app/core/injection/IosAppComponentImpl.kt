package org.hyperskill.app.core.injection

import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.analytic.injection.AnalyticComponentImpl
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.notification.remote.injection.IosPlatformPushNotificationsDataComponent
import org.hyperskill.app.notification.remote.injection.PlatformPushNotificationsDataComponent
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl

abstract class IosAppComponentImpl(
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant,
    sentryManager: SentryManager,
    analyticEngines: List<AnalyticEngine>
) : IosAppComponent, BaseAppGraph() {

    override val commonComponent: CommonComponent by lazy {
        CommonComponentImpl(buildVariant, userAgentInfo)
    }

    override val sentryComponent: SentryComponent by lazy {
        SentryComponentImpl(sentryManager)
    }

    override val analyticComponent: AnalyticComponent by lazy {
        AnalyticComponentImpl(
            appGraph = this,
            platformAnalyticEngines = analyticEngines
        )
    }

    override fun buildPlatformPushNotificationsDataComponent(): PlatformPushNotificationsDataComponent =
        IosPlatformPushNotificationsDataComponent(
            iosFCMTokenProvider = getIosFCMTokenProvider()
        )
}