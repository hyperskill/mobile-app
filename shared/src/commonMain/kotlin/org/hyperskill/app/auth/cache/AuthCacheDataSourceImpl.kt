package org.hyperskill.app.auth.cache

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import org.hyperskill.app.auth.cache.AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP
import org.hyperskill.app.auth.cache.AuthCacheKeyValues.AUTH_RESPONSE
import org.hyperskill.app.auth.cache.AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL
import org.hyperskill.app.auth.data.source.AuthCacheDataSource

class AuthCacheDataSourceImpl(
    private val settings: Settings
) : AuthCacheDataSource {
    override suspend fun isAuthorized(): Result<Boolean> =
        kotlin.runCatching {
            settings.get<String>(AUTH_RESPONSE) != null
        }

    override suspend fun clearCache() {
        kotlin.runCatching {
            settings.remove(AUTH_RESPONSE)
            settings.remove(AUTH_ACCESS_TOKEN_TIMESTAMP)
            settings.remove(AUTH_SOCIAL_ORDINAL)
        }
    }
}