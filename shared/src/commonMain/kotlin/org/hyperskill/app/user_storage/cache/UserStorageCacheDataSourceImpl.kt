package org.hyperskill.app.user_storage.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.user_storage.data.source.UserStorageCacheDataSource
import org.hyperskill.app.user_storage.domain.model.UserStorage

class UserStorageCacheDataSourceImpl(
    private val json: Json,
    private val settings: Settings
) : UserStorageCacheDataSource {
    override fun getUserStorage(): Result<UserStorage> =
        kotlin.runCatching {
            json.decodeFromString(settings[UserStorageCacheKeyValues.USER_STORAGE, ""])
        }

    override fun saveUserStorage(userStorage: UserStorage) {
        settings.putString(UserStorageCacheKeyValues.USER_STORAGE, json.encodeToString(userStorage))
    }

    override fun clearCache() {
        settings.remove(UserStorageCacheKeyValues.USER_STORAGE)
    }
}