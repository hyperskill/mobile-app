package org.hyperskill

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.hyperskill.app.network.injection.NetworkModule
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinDateSerializationTest {
    companion object {
        private const val TEST_DATE = "2017-07-13T16:59:00Z"
    }
    @Serializable
    data class InstantHolder(
        @SerialName("date")
        val instant: Instant
    )

    @Test
    fun dateSerializationTest() {
        val json = NetworkModule.provideJson()
        val serializedValue = json.encodeToJsonElement(InstantHolder(Instant.parse(TEST_DATE)))
        val expected = JsonObject(mapOf("date" to JsonPrimitive(TEST_DATE)))
        assertEquals(expected, serializedValue)
    }

    @Test
    fun dateDeserializationTest() {
        val json = NetworkModule.provideJson()
        val deserializedValue = json.decodeFromJsonElement<InstantHolder>(JsonObject(mapOf("date" to JsonPrimitive(TEST_DATE))))
        val expected = InstantHolder(Instant.parse(TEST_DATE))
        assertEquals(expected, deserializedValue)
    }
}