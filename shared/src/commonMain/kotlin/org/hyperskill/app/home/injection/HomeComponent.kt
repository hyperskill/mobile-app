package org.hyperskill.app.home.injection

import org.hyperskill.app.home.presentation.HomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface HomeComponent {
    val homeFeature: Feature<HomeFeature.ViewState, HomeFeature.Message, HomeFeature.Action>
}