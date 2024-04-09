package org.hyperskill.app.network.plugin.bearer_token

sealed interface TokenRefreshResult {
    object Success : TokenRefreshResult

    data class Failed(val shouldDeauthorizeUser: Boolean) : TokenRefreshResult
}