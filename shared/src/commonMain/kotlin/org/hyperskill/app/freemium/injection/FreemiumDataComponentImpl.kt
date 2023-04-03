package org.hyperskill.app.freemium.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

class FreemiumDataComponentImpl(
    appGraph: AppGraph
) : FreemiumDataComponent {
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        appGraph.singletonRepositoriesComponent.currentSubscriptionStateRepository

    override val freemiumInteractor: FreemiumInteractor
        get() = FreemiumInteractor(currentSubscriptionStateRepository)
}