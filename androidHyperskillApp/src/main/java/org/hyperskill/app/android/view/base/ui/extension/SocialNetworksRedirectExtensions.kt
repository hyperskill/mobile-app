package org.hyperskill.app.android.view.base.ui.extension

import android.content.Context
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect

fun SocialNetworksRedirect.redirectToUsernamePage(context: Context, username: String) {
    context.openUrl(this.baseUrl + username)
}