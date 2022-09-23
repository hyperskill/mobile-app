package org.hyperskill.app.auth.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import ru.nobird.app.presentation.redux.feature.Feature

class AuthSocialComponentImpl(
    private val commonComponent: CommonComponent,
    private val authComponent: AuthComponent,
    private val profileDataComponent: ProfileDataComponent,
    private val analyticComponent: AnalyticComponent
) : AuthSocialComponent {
    override val authSocialFeature: Feature<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action>
        get() = AuthSocialFeatureBuilder.build(
            authComponent.authInteractor,
            profileDataComponent.profileInteractor,
            analyticComponent.analyticInteractor
        )

    override val authSocialErrorMapper: AuthSocialErrorMapper
        get() = AuthSocialErrorMapper(commonComponent.resourceProvider)
}