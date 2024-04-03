package org.hyperskill.app.network.plugin.bearer_token

sealed class BearerTokenPluginException : Exception() {
    class TokenRefreshFailedException : BearerTokenPluginException()

    class TokenNotFoundException : BearerTokenPluginException()
}