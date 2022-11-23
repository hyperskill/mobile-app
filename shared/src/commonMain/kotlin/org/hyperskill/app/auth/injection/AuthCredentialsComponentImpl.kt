package org.hyperskill.app.auth.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.view.mapper.AuthCredentialsErrorMapper
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.sentry.injection.SentryComponent
import ru.nobird.app.presentation.redux.feature.Feature

class AuthCredentialsComponentImpl(
    private val commonComponent: CommonComponent,
    private val authComponent: AuthComponent,
    private val profileDataComponent: ProfileDataComponent,
    private val analyticComponent: AnalyticComponent,
    private val sentryComponent: SentryComponent
) : AuthCredentialsComponent {
    override val authCredentialsFeature: Feature<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action>
        get() = AuthCredentialsFeatureBuilder.build(
            authComponent.authInteractor,
            profileDataComponent.profileInteractor,
            analyticComponent.analyticInteractor,
            sentryComponent.sentryInteractor
        )

    override val authCredentialsErrorMapper: AuthCredentialsErrorMapper
        get() = AuthCredentialsErrorMapper(commonComponent.resourceProvider)
}