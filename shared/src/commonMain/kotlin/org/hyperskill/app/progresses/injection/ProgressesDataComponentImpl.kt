package org.hyperskill.app.progresses.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor

class ProgressesDataComponentImpl(
    appGraph: AppGraph
) : ProgressesDataComponent {
    override val progressesInteractor: ProgressesInteractor =
        ProgressesInteractor(appGraph.singletonRepositoriesComponent.progressesRepository)
}