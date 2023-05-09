package org.hyperskill.app.main.injection

import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface MainComponent {

    /**
     * Creates [AppFeature] with [initialState].
     * Initial state is required to restore the state on Android platform.
     */
    fun appFeature(
        initialState: AppFeature.State?
    ): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>

    /**
     * Special method for iOS platform to create [AppFeature] without initial state.
     * Internally calls [appFeature] with null as initialState.
     */
    fun appFeature(): Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>
}