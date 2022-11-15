package org.hyperskill.app.reactions.injection

import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor

interface ReactionsDataComponent {
    val reactionsInteractor: ReactionsInteractor
}