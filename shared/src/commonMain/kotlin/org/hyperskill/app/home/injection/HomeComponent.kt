package org.hyperskill.app.home.injection

import org.hyperskill.app.home.presentation.HomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface HomeComponent {
    val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
}