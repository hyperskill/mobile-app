package org.hyperskill.app.profile_settings.injection

import org.hyperskill.app.profile_settings.domain.interactor.ProfileSettingsInteractor
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Action
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.Message
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.State
import org.hyperskill.app.profile_settings.view.ProfileSettingsViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface ProfileSettingsComponent {
    val profileSettingsFeature: Feature<State, Message, Action>
    val profileSettingsInteractor: ProfileSettingsInteractor
    val profileSettingViewStateMapper: ProfileSettingsViewStateMapper
}