package org.hyperskill.profile_settings

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.profile_settings.domain.model.Theme

class ProfileSettingsSerializationTest {
    companion object {
        private const val TEST_JSON = """
            {
                "theme": "system"
            }
        """
    }

    @Test
    fun `Serialized profileSettings should be deserialized normally`() {
        val json = NetworkModule.provideJson()
        val expectedProfileSettings = ProfileSettings(Theme.SYSTEM)
        val actualProfileSettings = json.decodeFromString(ProfileSettings.serializer(), TEST_JSON)
        assertEquals(expectedProfileSettings, actualProfileSettings)
    }
}