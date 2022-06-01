package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import org.hyperskill.app.core.injection.CommonComponent
import ru.nobird.app.presentation.redux.feature.Feature

class AuthSocialComponentImpl(
    private val commonComponent: CommonComponent,
    private val authComponent: AuthComponent
) : AuthSocialComponent {
    override val authSocialFeature: Feature<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action>
        get() = AuthSocialFeatureBuilder.build(authComponent.authInteractor)

    override val authSocialErrorMapper: AuthSocialErrorMapper
        get() = AuthSocialErrorMapper(commonComponent.resourceProvider)
}