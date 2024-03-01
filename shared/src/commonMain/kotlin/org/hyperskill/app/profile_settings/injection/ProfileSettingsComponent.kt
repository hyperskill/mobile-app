package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProfileSettingsComponent {
    val profileSettingsFeature: Feature<ViewState, Message, Action>
    val profileSettingsInteractor: ProfileSettingsInteractor
}