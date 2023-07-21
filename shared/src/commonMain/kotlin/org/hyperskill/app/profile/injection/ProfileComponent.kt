package org.hyperskill.app.profile.injection

import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.view.BadgesViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface ProfileComponent {
    val profileFeature: Feature<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action>
    val badgesViewStateMapper: BadgesViewStateMapper
}