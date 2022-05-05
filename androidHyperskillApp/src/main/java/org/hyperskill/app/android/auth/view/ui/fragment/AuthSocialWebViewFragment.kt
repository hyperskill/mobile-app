package org.hyperskill.app.android.auth.view.ui.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.DialogInAppWebViewBinding
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.view.mapper.SocialAuthProviderRequestURLBuilder
import org.hyperskill.app.config.BuildKonfig
import ru.nobird.android.view.base.ui.extension.argument

class AuthSocialWebViewFragment : DialogFragment(R.layout.dialog_in_app_web_view) {

    companion object {
        const val TAG = "AuthSocialWebViewFragment"

        fun newInstance(provider: SocialAuthProvider): AuthSocialWebViewFragment =
            AuthSocialWebViewFragment().apply {
                this.provider = provider
            }
    }

    private var authCode: String? = null
    private val viewBinding by viewBinding(DialogInAppWebViewBinding::bind)

    private var provider: SocialAuthProvider by argument()
    private var webView: WebView? = null

    private var showError = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.dialog_in_app_web_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (webView == null) {
            webView = WebView(requireContext().applicationContext).also {
                it.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        if (request!!.url.toString().startsWith("https://" + BuildKonfig.HOST + "/oauth?")) {
                            val uri = Uri.parse(request.url.toString())
                            authCode = uri.getQueryParameter("code") ?: ""
                            (
                                activity as? Callback
                                    ?: parentFragment as? Callback
                                    ?: targetFragment as? Callback
                                )?.onSuccess(authCode!!, provider)
                            showError = false
                            dialog?.dismiss()
                        }
                        return false
                    }
                }
                @SuppressLint("SetJavaScriptEnabled")
                it.settings.javaScriptEnabled = true
            }

            viewBinding.centeredAppbar.centeredToolbar.centeredToolbar.apply {
                setNavigationOnClickListener {
                    if (showsDialog) {
                        dismiss()
                    } else {
                        activity?.finish()
                    }
                }
                setNavigationIcon(R.drawable.ic_close)
            }
        }
        webView.let { viewBinding.containerView.addView(it) }
        webView?.loadUrl(
            SocialAuthProviderRequestURLBuilder.build(provider)
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (
            activity as? Callback
                ?: parentFragment as? Callback
                ?: targetFragment as? Callback
            )?.onDismissed(showError)
    }

    override fun onDestroyView() {
        viewBinding.containerView.removeView(webView)
        super.onDestroyView()
    }

    interface Callback {
        fun onDismissed(showError: Boolean)
        fun onSuccess(authCode: String, provider: SocialAuthProvider)
    }
}