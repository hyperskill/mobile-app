package org.hyperskill.app.android.view.base.ui.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect

fun SocialNetworksRedirect.redirectToUsernamePage(context: Context, username: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(this.baseUrl + username)
    startActivity(context, intent, null)
}