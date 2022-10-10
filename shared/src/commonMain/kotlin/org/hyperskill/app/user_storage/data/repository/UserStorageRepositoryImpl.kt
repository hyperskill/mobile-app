package org.hyperskill.app.user_storage.data.repository

import kotlinx.serialization.json.jsonObject
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.user_storage.data.source.UserStorageCacheDataSource
import org.hyperskill.app.user_storage.data.source.UserStorageRemoteDataSource
import org.hyperskill.app.user_storage.domain.model.UserStorage
import org.hyperskill.app.user_storage.domain.model.UserStorageKey
import org.hyperskill.app.user_storage.domain.model.UserStorageValue
import org.hyperskill.app.user_storage.domain.repository.UserStorageRepository

class UserStorageRepositoryImpl(
    private val userStorageRemoteDataSource: UserStorageRemoteDataSource,
    private val userStorageCacheDataSource: UserStorageCacheDataSource
) : UserStorageRepository {
    override suspend fun getUserStorageValue(
        key: UserStorageKey,
        primarySourceType: DataSourceType
    ): Result<UserStorageValue> {
        val userStorage = when (primarySourceType) {
            DataSourceType.REMOTE ->
                userStorageRemoteDataSource
                    .getUserStorage()
                    .onSuccess { userStorage ->
                        userStorageCacheDataSource.saveUserStorage(userStorage)
                    }

            DataSourceType.CACHE ->
                userStorageCacheDataSource
                    .getUserStorage()
        }

        return getUserStorageValueByCompositeKey(key, userStorage)
    }

    override suspend fun updateUserStorage(key: UserStorageKey, value: UserStorageValue) {
        userStorageRemoteDataSource.updateUserStorage(key, value)
    }

    override fun clearCache() {
        userStorageCacheDataSource.clearCache()
    }

    private fun getUserStorageValueByCompositeKey(key: UserStorageKey, userStorage: Result<UserStorage>) =
        key.split(".").fold(userStorage as Result<UserStorageValue>) { jsonElementResult, keyPart ->
            jsonElementResult.getOrNull()?.jsonObject?.get(keyPart)?.let { Result.success(it) } ?: Result.failure(
                NoSuchElementException()
            )
        }
}