package org.hyperskill.app.auth.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.logging.inject.LoggerComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.sentry.injection.SentryComponent
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action as WebViewAction
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message as WebViewMessage
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State as WebViewState

class AuthSocialComponentImpl(
    private val commonComponent: CommonComponent,
    private val authComponent: AuthComponent,
    private val profileDataComponent: ProfileDataComponent,
    private val analyticComponent: AnalyticComponent,
    private val sentryComponent: SentryComponent,
    private val loggerComponent: LoggerComponent
) : AuthSocialComponent {
    override val authSocialFeature: Feature<State, Message, Action>
        get() = AuthSocialFeatureBuilder.build(
            authComponent.authInteractor,
            profileDataComponent.currentProfileStateRepository,
            analyticComponent.analyticInteractor,
            sentryComponent.sentryInteractor,
            loggerComponent.logger,
            commonComponent.buildKonfig.buildVariant
        )

    override val authSocialErrorMapper: AuthSocialErrorMapper
        get() = AuthSocialErrorMapper(commonComponent.resourceProvider)

    override val authSocialWebViewFeature: Feature<WebViewState, WebViewMessage, WebViewAction>
        get() = AuthSocialWebViewFeatureBuilder.build(
            logger = loggerComponent.logger,
            buildVariant = commonComponent.buildKonfig.buildVariant
        )
}