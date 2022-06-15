package org.hyperskill.app.android.core.view.ui.widget

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider

open class ExternalLinkWebViewClient(
    private val context: Context,
    private val resourceProvider: ResourceProvider
) : WebViewClient() {
    @Suppress("Deprecation")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        openExternalLink(Uri.parse(url))
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        openExternalLink(request.url)
        return true
    }

    private fun openExternalLink(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                resourceProvider.getString(SharedResources.strings.external_link_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}