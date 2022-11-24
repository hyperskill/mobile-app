package org.hyperskill.app.android.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.launchUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    startActivity(intent)
}