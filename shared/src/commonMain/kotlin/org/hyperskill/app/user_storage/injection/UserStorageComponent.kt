package org.hyperskill.app.user_storage.injection

import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import org.hyperskill.app.user_storage.domain.repository.UserStorageRepository

interface UserStorageComponent {
    val userStorageRepository: UserStorageRepository
    val userStorageInteractor: UserStorageInteractor
}