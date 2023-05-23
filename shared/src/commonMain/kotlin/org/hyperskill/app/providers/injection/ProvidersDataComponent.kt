package org.hyperskill.app.providers.injection

import org.hyperskill.app.providers.domain.interactor.ProvidersInteractor
import org.hyperskill.app.providers.domain.repository.ProvidersRepository

interface ProvidersDataComponent {
    val providersRepository: ProvidersRepository
    val providersInteractor: ProvidersInteractor
}