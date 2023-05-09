package org.hyperskill.app.android.util

import junit.framework.TestCase.assertEquals
import kotlinx.serialization.json.Json
import org.hyperskill.app.main.presentation.AppFeature
import org.junit.Test

class AppFeatureStateSerializationTest {

    @Test
    fun testAppFeatureStateEncoding() {
        AppFeature.State::class.sealedSubclasses.forEach { stateClass ->
            val state = when (stateClass) {
                AppFeature.State.Idle::class -> AppFeature.State.Idle
                AppFeature.State.Loading::class -> AppFeature.State.Loading
                AppFeature.State.NetworkError::class -> AppFeature.State.NetworkError
                AppFeature.State.Ready::class -> AppFeature.State.Ready(true)
                else -> throw IllegalStateException("Unknown state class: $stateClass. Please add it to the test.")
            }
            val json = Json.encodeToString(AppFeature.State.serializer(), state)
            val decodedState = Json.decodeFromString(AppFeature.State.serializer(), json)
            assertEquals(state, decodedState)
        }
    }
}