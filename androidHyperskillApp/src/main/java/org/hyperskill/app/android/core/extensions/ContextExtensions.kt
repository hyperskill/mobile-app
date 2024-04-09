package org.hyperskill.app.android.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import co.touchlab.kermit.Logger

fun Context.openUrl(
    url: String,
    logger: Logger
) {
    openUrl(Uri.parse(url), logger)
}

fun Context.openUrl(
    url: Uri,
    logger: Logger
) {
    openUrl(url) { e ->
        logger.e(e) {
            "Unable to open url in browser."
        }
        Toast.makeText(
            this,
            getString(org.hyperskill.app.R.string.external_link_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Context.openUrl(
    uri: Uri,
    onError: (e: Throwable) -> Unit
) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    try {
        startActivity(intent)
    // ActivityNotFoundException means there is no browser on the device
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
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