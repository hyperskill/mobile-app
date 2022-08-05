package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.profile.presentation.ProfileSettingsViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProfileSettingsComponentImpl(private val profileSettingsComponent: ProfileSettingsComponent) : PlatformProfileSettingsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(mapOf(ProfileSettingsViewModel::class.java to { ProfileSettingsViewModel(profileSettingsComponent.profileSettingsFeature.wrapWithViewContainer()) }))
}