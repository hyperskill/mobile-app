package org.hyperskill.app.android.auth.view.ui.widget

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.config.BuildKonfig

class AuthSocialWebViewClient(
    private val authSocialWebViewViewModel: AuthSocialWebViewViewModel,
    private val socialAuthProvider: SocialAuthProvider
) : WebViewClient() {

    companion object {
        private const val OAuthPath = "https://${BuildKonfig.HOST}/oauth?"
        private const val CodeParameter = "code"
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        request?.url?.let { url ->
            val urlString = url.toString()
            val codeQueryParameter = url.getQueryParameter(CodeParameter)
            if (urlString.startsWith(OAuthPath) && codeQueryParameter != null) {
                authSocialWebViewViewModel.onNewMessage(
                    AuthSocialWebViewFeature.Message.AuthCodeSuccess(
                        codeQueryParameter,
                        socialAuthProvider
                    )
                )
            }
        }
        return false
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        authSocialWebViewViewModel.onNewMessage(
            AuthSocialWebViewFeature.Message.AuthCodeFailure(
                socialError = AuthSocialError.CONNECTION_PROBLEM,
                originalError = if (error != null) Exception(error.description.toString()) else null
            )
        )
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        authSocialWebViewViewModel.onNewMessage(AuthSocialWebViewFeature.Message.PageLoaded)
    }
}