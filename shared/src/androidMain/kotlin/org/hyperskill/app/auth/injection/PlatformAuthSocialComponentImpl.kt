package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialViewModel
import org.hyperskill.app.core.injection.ManualViewModelFactory
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformAuthSocialComponentImpl(
    private val authSocialComponent: AuthSocialComponent
) : PlatformAuthSocialComponent {
    override val manualViewModelFactory: ManualViewModelFactory
        get() = ManualViewModelFactory(
            mapOf(
                AuthSocialViewModel::class.java to
                    { AuthSocialViewModel(authSocialComponent.authSocialFeature.wrapWithViewContainer()) }
            )
        )
}