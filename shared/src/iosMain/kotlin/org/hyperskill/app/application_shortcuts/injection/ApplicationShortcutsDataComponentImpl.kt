package org.hyperskill.app.application_shortcuts.injection

import org.hyperskill.app.application_shortcuts.domain.interactor.ApplicationShortcutsInteractor
import org.hyperskill.app.core.injection.AppGraph

internal class ApplicationShortcutsDataComponentImpl(
    private val appGraph: AppGraph
) : ApplicationShortcutsDataComponent {
    override val applicationShortcutsInteractor: ApplicationShortcutsInteractor
        get() = ApplicationShortcutsInteractor(
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            platform = appGraph.commonComponent.platform,
            userAgentInfo = appGraph.commonComponent.userAgentInfo,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}