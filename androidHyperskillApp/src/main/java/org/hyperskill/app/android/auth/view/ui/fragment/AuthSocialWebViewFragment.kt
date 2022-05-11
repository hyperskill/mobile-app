package org.hyperskill.app.android.auth.view.ui.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.android.databinding.DialogInAppWebViewBinding
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.auth.view.mapper.SocialAuthProviderRequestURLBuilder
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.view.mapper.ResourceProvider
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class AuthSocialWebViewFragment :
    DialogFragment(R.layout.dialog_in_app_web_view),
    ReduxView<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Action>
{
    companion object {
        const val TAG = "AuthSocialWebViewFragment"

        fun newInstance(provider: SocialAuthProvider): AuthSocialWebViewFragment =
            AuthSocialWebViewFragment().apply {
                this.provider = provider
            }
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var resourceProvider: ResourceProvider

    private val authSocialViewModel: AuthSocialWebViewViewModel by reduxViewModel(this) { viewModelFactory }

    private var authCode: String? = null
    private val viewBinding by viewBinding(DialogInAppWebViewBinding::bind)

    private var provider: SocialAuthProvider by argument()
    private var webView: WebView? = null

    private fun injectComponent() {
        HyperskillApp
            .component()
            .authSocialWebViewComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
    }

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
                            authSocialViewModel.onNewMessage(AuthSocialWebViewFeature.Message.AuthCodeSuccess(authCode!!, provider))
                            dialog?.dismiss()
                        }
                        return false
                    }
                }
                @SuppressLint("SetJavaScriptEnabled")
                it.settings.javaScriptEnabled = true
            }

        }
        webView.let { viewBinding.containerView.addView(it) }
        webView?.loadUrl(
            SocialAuthProviderRequestURLBuilder.build(provider)
        )

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

    override fun onDestroyView() {
        viewBinding.containerView.removeView(webView)
        super.onDestroyView()
    }

    override fun onAction(action: AuthSocialWebViewFeature.Action) {
        when (action) {
            is AuthSocialWebViewFeature.Action.CallbackAuthCode -> {
                (
                    activity as? Callback
                        ?: parentFragment as? Callback
                        ?: targetFragment as? Callback
                    )?.onSuccess(action.authCode, action.socialAuthProvider)
            }
            is AuthSocialWebViewFeature.Action.CallbackAuthError -> {
                (
                    activity as? Callback
                        ?: parentFragment as? Callback
                        ?: targetFragment as? Callback
                    )?.onError(action.socialError)
            }
        }
    }

    override fun render(state: AuthSocialWebViewFeature.State) {}

    interface Callback {
        fun onError(error: AuthSocialError)
        fun onSuccess(authCode: String, provider: SocialAuthProvider)
    }

}