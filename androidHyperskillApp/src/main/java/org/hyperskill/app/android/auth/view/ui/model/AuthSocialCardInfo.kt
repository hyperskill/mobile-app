package org.hyperskill.app.android.auth.view.ui.model

import org.hyperskill.app.android.R
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

enum class AuthSocialCardInfo(
    val textId: Int,
    val iconId: Int,
    val socialAuthProvider: SocialAuthProvider
) {
    JETBRAINS(
        R.string.auth_jetbrains_account_text,
        R.drawable.ic_jetbrains_logo,
        SocialAuthProvider.JETBRAINS_ACCOUNT
    ),
    GOOGLE(
        R.string.auth_google_account_text,
        R.drawable.ic_google_logo,
        SocialAuthProvider.GOOGLE
    ),
    GITHUB(
        R.string.auth_github_account_text,
        R.drawable.ic_github_logo,
        SocialAuthProvider.GITHUB
    )
}