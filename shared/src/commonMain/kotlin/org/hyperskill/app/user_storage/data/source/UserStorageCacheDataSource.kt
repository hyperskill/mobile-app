package org.hyperskill.app.user_storage.data.source

import org.hyperskill.app.user_storage.domain.model.UserStorage

interface UserStorageCacheDataSource {
    fun getUserStorage(): Result<UserStorage>
    fun saveUserStorage(userStorage: UserStorage)
    fun clearCache()
}