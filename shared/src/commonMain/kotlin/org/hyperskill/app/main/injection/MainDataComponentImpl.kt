package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.domain.interactor.AppInteractor

class MainDataComponentImpl(private val appGraph: AppGraph) : MainDataComponent {
    override val appInteractor: AppInteractor
        get() = AppInteractor(
            appGraph.authComponent.authInteractor,
            appGraph.buildProfileDataComponent().currentProfileStateRepository,
            appGraph.buildUserStorageComponent().userStorageInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.buildProgressesDataComponent().progressesRepository,
            appGraph.buildTrackDataComponent().trackRepository,
            appGraph.buildProvidersDataComponent().providersRepository,
            appGraph.buildProjectsDataComponent().projectsRepository
        )
}