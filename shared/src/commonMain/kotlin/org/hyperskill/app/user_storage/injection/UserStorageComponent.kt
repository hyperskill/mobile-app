package org.hyperskill.app.user_storage.injection

import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

interface UserStorageComponent {
    val userStorageInteractor: UserStorageInteractor
}