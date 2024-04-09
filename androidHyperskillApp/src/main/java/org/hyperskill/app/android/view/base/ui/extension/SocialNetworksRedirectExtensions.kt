package org.hyperskill.app.android.view.base.ui.extension

import android.content.Context
import co.touchlab.kermit.Logger
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect

fun SocialNetworksRedirect.redirectToUsernamePage(context: Context, username: String, logger: Logger) {
    context.openUrl(url = this.baseUrl + username, logger)
}