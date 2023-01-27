package org.hyperskill.app.progresses.injection

import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository

interface ProgressesDataComponent {
    val progressesRepository: ProgressesRepository
    val progressesInteractor: ProgressesInteractor
}