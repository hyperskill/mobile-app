package org.hyperskill.app.android.auth.view.ui.model

import org.hyperskill.app.android.R

enum class AuthSocialCardInfo(
    val textId: Int,
    val iconId: Int,
    val provider: String
) {
    JETBRAINS(
        R.string.auth_jetbrains_account_text,
        R.drawable.ic_jetbrains_logo,
        "jetbrains"
    ),
    GOOGLE(
        R.string.auth_google_account_text,
        R.drawable.ic_google_logo,
        "google"
    ),
    GITHUB(
        R.string.auth_github_account_text,
        R.drawable.ic_github_logo,
        "github"
    )
}