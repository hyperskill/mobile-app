package org.hyperskill.app.android.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.openUrl(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            getString(org.hyperskill.app.R.string.external_link_error, uri),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Context.openUrl(url: String) {
    openUrl(Uri.parse(url))
}

/**
 * Find the closest Activity in a given Context.
 */
tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> throw IllegalStateException("Can't find Activity in a given context")
    }