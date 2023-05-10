package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthCredentialsViewModel
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformAuthCredentialsComponentImpl(
    private val authCredentialsComponent: AuthCredentialsComponent
) : PlatformAuthCredentialsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                AuthCredentialsViewModel::class.java to {
                    AuthCredentialsViewModel(authCredentialsComponent.authCredentialsFeature.wrapWithViewContainer())
                }
            )
        )
}