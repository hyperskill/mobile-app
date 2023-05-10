package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer
import ru.nobird.app.presentation.redux.feature.Feature

class PlatformAuthSocialWebViewComponentImpl : PlatformAuthSocialWebViewComponent {
    override val feature: Feature<
        AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>
        get() = AuthSocialWebViewFeatureBuilder.build()

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                AuthSocialWebViewViewModel::class.java to
                    { AuthSocialWebViewViewModel(feature.wrapWithViewContainer()) }
            )
        )
}