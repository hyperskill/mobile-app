package org.hyperskill.app.auth.data.source

interface AuthCacheDataSource {
    suspend fun isAuthorized(): Result<Boolean>
    suspend fun clearCache()
}