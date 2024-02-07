package org.hyperskill.app.application_shortcuts.injection

import org.hyperskill.app.application_shortcuts.domain.interactor.ApplicationShortcutsInteractor

interface ApplicationShortcutsDataComponent {
    val applicationShortcutsInteractor: ApplicationShortcutsInteractor
}