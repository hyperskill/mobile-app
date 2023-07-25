package org.hyperskill.app.android.auth.view.ui.widget

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

class AuthSocialWebViewClient(
    private val socialAuthProvider: SocialAuthProvider,
    private val networkEndpointConfigInfo: NetworkEndpointConfigInfo,
    private val onNewMessage: (AuthSocialWebViewFeature.Message) -> Unit
) : WebViewClient() {

    companion object {
        private const val CodeParameter = "code"
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        request?.url?.let { url ->
            val urlString = url.toString()
            if (urlString.startsWith("https://${networkEndpointConfigInfo.host}/oauth?")) {
                try {
                    url.getQueryParameter(CodeParameter)?.let { codeQueryParameter ->
                        onNewMessage(
                            AuthSocialWebViewFeature.Message.AuthCodeSuccess(
                                codeQueryParameter,
                                socialAuthProvider
                            )
                        )
                    }
                } catch (e: UnsupportedOperationException) {
                    onNewMessage(
                        AuthSocialWebViewFeature.Message.AuthCodeFailure(
                            socialError = AuthSocialError.CONNECTION_PROBLEM,
                            originalError = e
                        )
                    )
                }
                onNewMessage(message)
            }
        }
        return false
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (request?.isForMainFrame == true) {
            onNewMessage(
                AuthSocialWebViewFeature.Message.AuthCodeFailure(
                    socialError = AuthSocialError.CONNECTION_PROBLEM,
                    originalError = if (error != null) Exception(error.description.toString()) else null
                )
            )
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        onNewMessage(AuthSocialWebViewFeature.Message.PageLoaded)
    }
}