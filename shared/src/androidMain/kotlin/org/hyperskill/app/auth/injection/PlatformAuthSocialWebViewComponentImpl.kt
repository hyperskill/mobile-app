package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformAuthSocialWebViewComponentImpl(
    private val authSocialComponent: AuthSocialComponent
) : PlatformAuthSocialWebViewComponent {

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                AuthSocialWebViewViewModel::class.java to {
                    AuthSocialWebViewViewModel(
                        authSocialComponent.authSocialWebViewFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}