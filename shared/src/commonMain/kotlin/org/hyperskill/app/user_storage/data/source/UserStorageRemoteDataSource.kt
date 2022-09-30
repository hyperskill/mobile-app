package org.hyperskill.app.user_storage.data.source

import org.hyperskill.app.user_storage.domain.model.UserStorage
import org.hyperskill.app.user_storage.domain.model.UserStorageKey
import org.hyperskill.app.user_storage.domain.model.UserStorageValue

interface UserStorageRemoteDataSource {
    suspend fun getUserStorage(): Result<UserStorage>
    suspend fun updateUserStorage(key: UserStorageKey, value: UserStorageValue): Result<UserStorage>
}