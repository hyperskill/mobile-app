package org.hyperskill.app.main.injection

import org.hyperskill.app.main.domain.interactor.AppInteractor

interface MainDataComponent {
    val appInteractor: AppInteractor
}