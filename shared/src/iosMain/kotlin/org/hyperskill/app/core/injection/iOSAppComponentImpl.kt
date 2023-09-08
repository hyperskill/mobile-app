package org.hyperskill.app.core.injection

import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.notification.remote.injection.IosPlatformPushNotificationsDataComponent
import org.hyperskill.app.notification.remote.injection.PlatformPushNotificationsDataComponent
import org.hyperskill.app.sentry.domain.model.manager.SentryManager
import org.hyperskill.app.sentry.injection.SentryComponent
import org.hyperskill.app.sentry.injection.SentryComponentImpl

class iOSAppComponentImpl(
    userAgentInfo: UserAgentInfo,
    buildVariant: BuildVariant,
    sentryManager: SentryManager
) : iOSAppComponent, BaseAppGraph() {

    override val commonComponent: CommonComponent =
        CommonComponentImpl(buildVariant, userAgentInfo)

    override val sentryComponent: SentryComponent =
        SentryComponentImpl(sentryManager)

    override fun buildPlatformPushNotificationsDataComponent(): PlatformPushNotificationsDataComponent =
        IosPlatformPushNotificationsDataComponent()
}