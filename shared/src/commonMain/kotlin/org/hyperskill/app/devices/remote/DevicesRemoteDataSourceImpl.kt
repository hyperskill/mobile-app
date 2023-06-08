package org.hyperskill.app.devices.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.remote.model.DevicesRequest

class DevicesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : DevicesRemoteDataSource {
    override suspend fun createDevice(
        registrationId: String,
        isActive: Boolean,
        type: String
    ): Result<Device> =
        kotlin.runCatching {
            httpClient
                .post("/api/devices") {
                    contentType(ContentType.Application.Json)
                    setBody(DevicesRequest(registrationId = registrationId, isActive = isActive, type = type))
                }
                .body()
        }
}