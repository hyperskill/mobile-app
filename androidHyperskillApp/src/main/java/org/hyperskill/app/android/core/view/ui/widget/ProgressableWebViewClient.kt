package org.hyperskill.app.android.core.view.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import androidx.core.view.isVisible

class ProgressableWebViewClient(
    private val progressView: View,
    context: Context = progressView.context
) : ExternalLinkWebViewClient(context) {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressView.isVisible = true
        view?.isVisible = false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        progressView.isVisible = false
        view?.isVisible = true
        super.onPageFinished(view, url)
    }
}