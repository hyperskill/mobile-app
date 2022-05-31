package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthCredentialsViewModel
import org.hyperskill.app.core.injection.ManualViewModelFactory
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformAuthCredentialsComponentImpl(
    private val authCredentialsComponent: AuthCredentialsComponent
) : PlatformAuthCredentialsComponent {
    override val manualViewModelFactory: ManualViewModelFactory
        get() = ManualViewModelFactory(mapOf(AuthCredentialsViewModel::class.java to { AuthCredentialsViewModel(authCredentialsComponent.authCredentialsFeature.wrapWithViewContainer()) } ))
}