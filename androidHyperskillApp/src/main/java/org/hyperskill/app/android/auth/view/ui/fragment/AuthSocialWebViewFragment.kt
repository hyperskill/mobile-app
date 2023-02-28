package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.widget.AuthSocialWebViewClient
import org.hyperskill.app.android.databinding.DialogInAppWebViewBinding
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.auth.view.mapper.SocialAuthProviderRequestURLBuilder
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class AuthSocialWebViewFragment :
    DialogFragment(R.layout.dialog_in_app_web_view),
    ReduxView<AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Action> {
    companion object {
        const val TAG = "AuthSocialWebViewFragment"

        fun newInstance(provider: SocialAuthProvider): AuthSocialWebViewFragment =
            AuthSocialWebViewFragment().apply {
                this.provider = provider
            }
    }

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var networkEndpointConfigInfo: NetworkEndpointConfigInfo

    private val authSocialWebViewViewModel: AuthSocialWebViewViewModel by reduxViewModel(this) { viewModelFactory }

    private var _binding: DialogInAppWebViewBinding? = null
    private val viewBinding get() = _binding!!

    private var provider: SocialAuthProvider by argument()
    private var webView: WebView? = null

    private val viewStateDelegate = ViewStateDelegate<AuthSocialWebViewFeature.State>()

    private fun injectComponent() {
        val authSocialWebViewComponent = HyperskillApp.graph().buildPlatformAuthSocialWebViewComponent()
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
        viewModelFactory = authSocialWebViewComponent.reduxViewModelFactory
        networkEndpointConfigInfo = HyperskillApp.graph().networkComponent.endpointConfigInfo
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<AuthSocialWebViewFeature.State.Idle>()
        viewStateDelegate.addState<AuthSocialWebViewFeature.State.Loading>(viewBinding.loadingProgressBar.root)
        viewStateDelegate.addState<AuthSocialWebViewFeature.State.Content>(webView as View)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogInAppWebViewBinding.inflate(inflater, container, false)
        if (webView == null) {
            webView = WebView(requireContext().applicationContext).also {
                it.webViewClient = AuthSocialWebViewClient(
                    authSocialWebViewViewModel,
                    provider,
                    networkEndpointConfigInfo
                )
                it.settings.apply {
                    javaScriptEnabled = true

                    /**
                     * Google disable authentication from webView
                     * Remove "wv" substring (means "webView") from userAgent to be able to login via Google
                     * */
                    userAgentString = userAgentString?.replace("wv", "")
                }
            }
        }
        webView?.let { binding.containerView.addView(it) }
        this._binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewStateDelegate()
        viewBinding.centeredAppbar.centeredToolbar.centeredToolbar.apply {
            setNavigationOnClickListener {
                dismiss()
            }
            setNavigationIcon(R.drawable.ic_close)
        }
        authSocialWebViewViewModel.onNewMessage(
            AuthSocialWebViewFeature.Message.InitMessage(
                SocialAuthProviderRequestURLBuilder.build(provider, networkEndpointConfigInfo)
            )
        )
    }

    override fun onStart() {
        super.onStart()
        dialog
            ?.window
            ?.let { window ->
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
    }

    override fun onDestroyView() {
        viewBinding.containerView.removeView(webView)
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        webView = null
        super.onDestroy()
    }

    override fun onAction(action: AuthSocialWebViewFeature.Action) {
        when (action) {
            is AuthSocialWebViewFeature.Action.CallbackAuthCode -> {
                (parentFragment as? Callback)?.onSuccess(action.authCode, action.socialAuthProvider)
                dialog?.dismiss()
            }
            is AuthSocialWebViewFeature.Action.CallbackAuthError -> {
                (parentFragment as? Callback)?.onError(action.socialError, action.originalError)
                dialog?.dismiss()
            }
        }
    }

    override fun render(state: AuthSocialWebViewFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is AuthSocialWebViewFeature.State.Loading) {
            webView?.loadUrl(state.url)
        }
    }

    interface Callback {
        fun onError(error: AuthSocialError, originalError: Throwable?)
        fun onSuccess(authCode: String, provider: SocialAuthProvider)
    }
}