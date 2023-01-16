package org.hyperskill.app.network.domain.model

class NetworkEndpointConfigInfo(
    val baseUrl: String,
    val host: String,
    val oauthClientId: String,
    val oauthClientSecret: String,
    val redirectUri: String,
    val credentialsClientId: String,
    val credentialsClientSecret: String
)