package org.hyperskill.app.android.core.view.ui.widget

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.hyperskill.app.android.core.extensions.openUrl

open class ExternalLinkWebViewClient(
    private val context: Context
) : WebViewClient() {
    @Suppress("Deprecation")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        context.openUrl(url)
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        context.openUrl(request.url)
        return true
    }
}