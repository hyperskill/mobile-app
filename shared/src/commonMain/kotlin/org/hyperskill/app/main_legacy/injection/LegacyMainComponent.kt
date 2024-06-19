package org.hyperskill.app.main_legacy.injection

import org.hyperskill.app.main_legacy.presentation.LegacyAppFeature
import ru.nobird.app.presentation.redux.feature.Feature

@Deprecated("Should be removed in ALTAPPS-1276")
interface LegacyMainComponent {

    /**
     * Special method for iOS platform to create [LegacyAppFeature] without initial state.
     * Internally calls [legacyAppFeature] with null as initialState.
     */
    fun legacyAppFeature(): Feature<LegacyAppFeature.State, LegacyAppFeature.Message, LegacyAppFeature.Action>
}