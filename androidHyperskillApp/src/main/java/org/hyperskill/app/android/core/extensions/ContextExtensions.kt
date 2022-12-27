package org.hyperskill.app.android.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.hyperskill.app.android.R

fun Context.openUrl(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            this,
            getString(R.string.external_link_error, uri),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Context.openUrl(url: String) {
    openUrl(Uri.parse(url))
}