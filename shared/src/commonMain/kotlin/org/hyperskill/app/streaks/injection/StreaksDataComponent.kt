package org.hyperskill.app.streaks.injection

import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor

interface StreaksDataComponent {
    val streaksInteractor: StreaksInteractor
}