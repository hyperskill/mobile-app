package org.hyperskill.app.streak.injection

import org.hyperskill.app.streak.domain.interactor.StreaksInteractor

interface StreaksDataComponent {
    val streaksInteractor: StreaksInteractor
}