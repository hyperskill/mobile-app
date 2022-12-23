package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.view.mapper.PlaceholderNewUserViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface PlaceholderNewUserComponent {
    val placeholderNewUserFeature: Feature<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Message, PlaceholderNewUserFeature.Action>

    val placeHolderNewUserViewDataMapper: PlaceholderNewUserViewDataMapper
}