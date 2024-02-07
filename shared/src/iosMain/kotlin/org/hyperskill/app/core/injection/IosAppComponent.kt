package org.hyperskill.app.core.injection

import org.hyperskill.app.application_shortcuts.injection.ApplicationShortcutsDataComponent
import org.hyperskill.app.notification.remote.data.repository.IosFCMTokenProvider

interface IosAppComponent : AppGraph {
    fun getIosFCMTokenProvider(): IosFCMTokenProvider

    fun buildApplicationShortcutsDataComponent(): ApplicationShortcutsDataComponent
}