package org.hyperskill.app.progress_screen.injection

import org.hyperskill.app.progress_screen.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progress_screen.domain.repository.ProgressesRepository

interface ProgressesDataComponent {
    val progressesRepository: ProgressesRepository
    val progressesInteractor: ProgressesInteractor
}