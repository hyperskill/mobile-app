package org.hyperskill.app.core.injection

import org.hyperskill.app.application_shortcuts.injection.ApplicationShortcutsDataComponent
import org.hyperskill.app.notification.remote.data.repository.IosFCMTokenProvider
import org.hyperskill.app.purchases.domain.manager.IosPurchaseManager

interface IosAppComponent : AppGraph {
    fun getIosFCMTokenProvider(): IosFCMTokenProvider

    fun getIosPurchaseManager(): IosPurchaseManager

    fun buildApplicationShortcutsDataComponent(): ApplicationShortcutsDataComponent
}