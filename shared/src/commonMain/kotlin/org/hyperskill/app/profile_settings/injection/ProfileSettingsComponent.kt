package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface ProfileSettingsComponent {
    val profileSettingsFeature: Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
    val profileSettingsInteractor: ProfileSettingsInteractor
}