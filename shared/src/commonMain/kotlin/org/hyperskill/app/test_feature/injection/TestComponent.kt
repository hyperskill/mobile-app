package org.hyperskill.app.test_feature

import org.hyperskill.app.test_feature.TestFeature
import org.hyperskill.app.test_feature.TestFeature.Action
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

interface TestComponent {
    val test: Feature<State, Message, Action>
}