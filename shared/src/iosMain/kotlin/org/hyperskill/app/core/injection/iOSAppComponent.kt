package org.hyperskill.app.core.injection

import org.hyperskill.app.notification.remote.data.repository.IosFCMTokenProvider

interface iOSAppComponent : AppGraph {
    fun getIosFCMTokenProvider(): IosFCMTokenProvider
}