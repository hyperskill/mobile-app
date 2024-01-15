package org.hyperskill.device

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.DeviceType
import org.hyperskill.app.network.injection.NetworkModule

class DeviceSerializationTest {
    companion object {
        private const val TEST_JSON = """
            {
                "name": "Android 33",
                "registration_id": "long_registration_id",
                "active": true,
                "type": "some_unknown_type"
            }
        """

        private val EXPECTED_DEVICE: Device =
            Device(
                name = "Android 33",
                registrationId = "long_registration_id",
                isActive = true,
                type = DeviceType.UNKNOWN
            )
    }

    @Test
    fun `Serialized device should be deserialized normally`() {
        val json = NetworkModule.provideJson()
        val actualDevice = json.decodeFromString(Device.serializer(), TEST_JSON)
        assertEquals(EXPECTED_DEVICE, actualDevice)
    }
}