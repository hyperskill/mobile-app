package org.hyperskill.app.progresses.injection

import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor

interface ProgressesDataComponent {
    val progressesInteractor: ProgressesInteractor
}