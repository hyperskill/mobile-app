package org.hyperskill.app.auth.domain.model

enum class SocialAuthProvider(val title: String, val isSdk: Boolean) {
    JETBRAINS_ACCOUNT("jetbrains", false),
    GOOGLE("google", true),
    GITHUB("github", false),
    APPLE("apple", true)
}