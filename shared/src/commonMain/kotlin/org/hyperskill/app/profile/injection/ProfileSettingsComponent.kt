package org.hyperskill.app.profile.injection

import org.hyperskill.app.profile.presentation.ProfileSettingsFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface ProfileSettingsComponent {
    val profileSettingsFeature: Feature<ProfileSettingsFeature.State, ProfileSettingsFeature.Message, ProfileSettingsFeature.Action>
}