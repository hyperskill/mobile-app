package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.profile.presentation.ProfileViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProfileComponentImpl(private val profileComponent: ProfileComponent) : PlatformProfileComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                ProfileViewModel::class.java to {
                    ProfileViewModel(profileComponent.profileFeature.wrapWithViewContainer())
                }
            )
        )
}