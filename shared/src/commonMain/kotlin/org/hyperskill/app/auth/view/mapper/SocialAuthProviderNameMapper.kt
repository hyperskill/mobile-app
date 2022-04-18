package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.model.SocialAuthProvider.APPLE
import org.hyperskill.app.auth.domain.model.SocialAuthProvider.GITHUB
import org.hyperskill.app.auth.domain.model.SocialAuthProvider.GOOGLE
import org.hyperskill.app.auth.domain.model.SocialAuthProvider.JETBRAINS_ACCOUNT

object SocialAuthProviderNameMapper {
    fun getName(provider: SocialAuthProvider): String =
        when (provider) {
            JETBRAINS_ACCOUNT -> "jetbrains"
            GOOGLE -> "google"
            GITHUB -> "github"
            APPLE -> "apple"
        }
}