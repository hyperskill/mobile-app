package org.hyperskill.app.user_storage.domain.repository

import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.user_storage.domain.model.UserStorageKey
import org.hyperskill.app.user_storage.domain.model.UserStorageValue

interface UserStorageRepository {
    suspend fun getUserStorageValue(
        key: UserStorageKey,
        primarySourceType: DataSourceType = DataSourceType.CACHE
    ): Result<UserStorageValue>

    suspend fun updateUserStorage(key: UserStorageKey, value: UserStorageValue)

    fun clearCache()
}