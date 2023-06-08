package org.hyperskill.app.devices.injection

import org.hyperskill.app.devices.domain.repository.DevicesRepository

interface DevicesDataComponent {
    val devicesRepository: DevicesRepository
}