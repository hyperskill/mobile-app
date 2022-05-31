package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.view.mapper.AuthCredentialsErrorMapper
import org.hyperskill.app.core.injection.CommonComponent
import ru.nobird.app.presentation.redux.feature.Feature

class AuthCredentialsComponentImpl(
    private val commonComponent: CommonComponent,
    private val authComponent: AuthComponent
) : AuthCredentialsComponent {
    override val authCredentialsFeature: Feature<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action>
        get() = AuthCredentialsFeatureBuilder.build(authComponent.authInteractor)

    override val authCredentialsErrorMapper: AuthCredentialsErrorMapper
        get() = AuthCredentialsErrorMapper(commonComponent.resourceProvider)
}