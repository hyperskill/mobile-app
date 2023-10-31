package org.hyperskill.app.freemium.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumDataComponentImpl(
    appGraph: AppGraph
) : FreemiumDataComponent {
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository

    private val currentProfileStateRepository: CurrentProfileStateRepository =
        appGraph.profileDataComponent.currentProfileStateRepository

    override val freemiumInteractor: FreemiumInteractor
        get() = FreemiumInteractor(currentSubscriptionStateRepository, currentProfileStateRepository)
}