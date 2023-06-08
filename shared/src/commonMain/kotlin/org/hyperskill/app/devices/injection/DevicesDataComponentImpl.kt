package org.hyperskill.app.devices.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.devices.cache.DevicesCacheDataSourceImpl
import org.hyperskill.app.devices.data.repository.DevicesRepositoryImpl
import org.hyperskill.app.devices.data.source.DevicesCacheDataSource
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.repository.DevicesRepository
import org.hyperskill.app.devices.remote.DevicesRemoteDataSourceImpl

class DevicesDataComponentImpl(appGraph: AppGraph) : DevicesDataComponent {
    private val devicesCacheDataSource: DevicesCacheDataSource = DevicesCacheDataSourceImpl(
        appGraph.commonComponent.json,
        appGraph.commonComponent.settings
    )
    private val devicesRemoteDataSource: DevicesRemoteDataSource = DevicesRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )

    override val devicesRepository: DevicesRepository
        get() = DevicesRepositoryImpl(
            devicesCacheDataSource,
            devicesRemoteDataSource
        )
}