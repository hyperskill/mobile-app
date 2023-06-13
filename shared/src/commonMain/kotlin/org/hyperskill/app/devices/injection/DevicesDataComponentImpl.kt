package org.hyperskill.app.devices.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.devices.cache.CurrentDeviceCacheDataSourceImpl
import org.hyperskill.app.devices.data.repository.DevicesRepositoryImpl
import org.hyperskill.app.devices.data.source.CurrentDeviceCacheDataSource
import org.hyperskill.app.devices.data.source.DevicesRemoteDataSource
import org.hyperskill.app.devices.domain.repository.DevicesRepository
import org.hyperskill.app.devices.remote.DevicesRemoteDataSourceImpl

class DevicesDataComponentImpl(appGraph: AppGraph) : DevicesDataComponent {
    private val currentDeviceCacheDataSource: CurrentDeviceCacheDataSource =
        CurrentDeviceCacheDataSourceImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )
    private val devicesRemoteDataSource: DevicesRemoteDataSource =
        DevicesRemoteDataSourceImpl(
            appGraph.networkComponent.authorizedHttpClient
        )

    override val devicesRepository: DevicesRepository
        get() = DevicesRepositoryImpl(
            devicesRemoteDataSource,
            currentDeviceCacheDataSource
        )
}