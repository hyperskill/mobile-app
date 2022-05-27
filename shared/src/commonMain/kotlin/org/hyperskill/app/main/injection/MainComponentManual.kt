package org.hyperskill.app.main.injection

import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface MainComponentManual {
    val appFeature: Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>
}