package org.hyperskill.app.android.core.view.ui.widget

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import co.touchlab.kermit.Logger
import org.hyperskill.app.android.core.extensions.logger
import org.hyperskill.app.android.core.extensions.openUrl

open class ExternalLinkWebViewClient(
    private val context: Context
) : WebViewClient() {

    companion object {
        private const val LOG_TAG = "ExternalLinkWebViewClient"
    }

    private val logger: Logger by logger(LOG_TAG)

    @Suppress("OVERRIDE_DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        context.openUrl(url, logger)
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        context.openUrl(request.url, logger)
        return true
    }
}