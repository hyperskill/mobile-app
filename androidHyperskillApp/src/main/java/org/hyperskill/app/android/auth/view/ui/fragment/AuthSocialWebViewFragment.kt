package org.hyperskill.app.android.auth.view.ui.fragment

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialWebViewBinding
import org.hyperskill.app.config.BuildKonfig

class AuthSocialWebViewFragment(private val siteName: String) : DialogFragment(R.layout.fragment_auth_social_web_view) {

    companion object {
        const val TAG = "AuthSocialWebViewFragment"

        fun newInstance(siteName: String): AuthSocialWebViewFragment =
            AuthSocialWebViewFragment(siteName)
    }

    private val viewBinding by viewBinding(FragmentAuthSocialWebViewBinding::bind)
    private var authCode: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = viewBinding.authSocialWebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (
                    request!!.url.toString().startsWith("https://hyperskill.org/") &&
                    request.url.toString().contains("callback")
                ) {
                    val uri = Uri.parse(request.url.toString())
                    authCode = uri.getQueryParameter("code") ?: ""
                    (
                        activity as? Callback ?:
                        parentFragment as? Callback ?:
                        targetFragment as? Callback
                    )?.onSuccess(authCode!!)
                }
                return false
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(
            "https://hyperskill.org/accounts/" + siteName +
                "/login?next=/oauth2/authorize/?client_id=" + BuildKonfig.OAUTH_CLIENT_ID +
                "&response_type=code"
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (authCode == null) {
            (
                activity as? Callback ?:
                parentFragment as? Callback ?:
                targetFragment as? Callback
            )?.onDismissed()
        }
    }

    interface Callback {
        fun onDismissed()
        fun onSuccess(authCode: String)
    }
}