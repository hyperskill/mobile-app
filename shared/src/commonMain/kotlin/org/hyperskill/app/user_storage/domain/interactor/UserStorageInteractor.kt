package org.hyperskill.app.user_storage.domain.interactor

import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.user_storage.domain.model.UserStorageKey
import org.hyperskill.app.user_storage.domain.model.UserStorageValue
import org.hyperskill.app.user_storage.domain.repository.UserStorageRepository

class UserStorageInteractor(
    private val userStorageRepository: UserStorageRepository
) {
    suspend fun getUserStorageValue(key: UserStorageKey, primarySourceType: DataSourceType = DataSourceType.CACHE): Result<UserStorageValue> =
        userStorageRepository.getUserStorageValue(key, primarySourceType)

    suspend fun updateUserStorage(key: UserStorageKey, value: UserStorageValue) {
        userStorageRepository.updateUserStorage(key, value)
    }

    fun clearCache() {
        userStorageRepository.clearCache()
    }
}