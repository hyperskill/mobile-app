package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Scope
import com.google.android.material.snackbar.Snackbar
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.adapter.delegates.AuthSocialAdapterDelegate
import org.hyperskill.app.android.auth.view.ui.model.AuthSocialCardInfo
import org.hyperskill.app.android.auth.view.ui.navigation.AuthEmailScreen
import org.hyperskill.app.android.auth.view.ui.navigation.AuthFlow
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.android.sentry.domain.model.SentryBreadcrumbKeyValues
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.presentation.AuthSocialViewModel
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import org.hyperskill.app.core.view.mapper.ResourceProvider
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class AuthSocialFragment :
    Fragment(R.layout.fragment_auth_social),
    ReduxView<AuthSocialFeature.State, AuthSocialFeature.Action.ViewAction>,
    AuthSocialWebViewFragment.Callback {

    companion object {
        fun newInstance(): AuthSocialFragment =
            AuthSocialFragment()
    }

    private lateinit var resourceProvider: ResourceProvider

    private lateinit var authSocialErrorMapper: AuthSocialErrorMapper

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val authSocialViewModel: AuthSocialViewModel by reduxViewModel(this) { viewModelFactory }

    private val viewBinding by viewBinding(FragmentAuthSocialBinding::bind)
    private val authMaterialCardViewsAdapter: DefaultDelegateAdapter<AuthSocialCardInfo> = DefaultDelegateAdapter()

    private var currentSocialAuthProvider: SocialAuthProvider? = null

    private val loadingProgressDialogFragment: DialogFragment =
        LoadingProgressDialogFragment.newInstance()

    private val signInWithGoogleCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            onSuccess(authCode!!, SocialAuthProvider.GOOGLE)
        } catch (e: ApiException) {
            logSignInErrorToSentry(null, e)

            val message = if (e.statusCode == CommonStatusCodes.NETWORK_ERROR)
                resourceProvider.getString(SharedResources.strings.connection_error)
            else resourceProvider.getString(SharedResources.strings.common_error)

            view?.snackbar(message, Snackbar.LENGTH_LONG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        authMaterialCardViewsAdapter += AuthSocialAdapterDelegate(::onSocialClickListener)
    }

    private fun injectComponent() {
        val authSocialComponent = HyperskillApp.graph().buildAuthSocialComponent()
        val platformSocialComponent = HyperskillApp.graph().buildPlatformAuthSocialComponent(authSocialComponent)
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
        authSocialErrorMapper = authSocialComponent.authSocialErrorMapper
        viewModelFactory = platformSocialComponent.reduxViewModelFactory
    }

    private fun onSocialClickListener(social: AuthSocialCardInfo) {
        currentSocialAuthProvider = social.socialAuthProvider
        authSocialViewModel.onNewMessage(AuthSocialFeature.Message.ClickedSignInWithSocialEventMessage(social.socialAuthProvider))

        val breadcrumb = Breadcrumb().apply {
            category = SentryBreadcrumbKeyValues.CATEGORY_AUTH_SOCIAL
            message = "Signing in with ${social.socialAuthProvider.title}"
            level = SentryLevel.INFO
        }
        Sentry.addBreadcrumb(breadcrumb)

        val authSocialWebViewFragment = AuthSocialWebViewFragment.newInstance(social.socialAuthProvider)
        when (social) {
            AuthSocialCardInfo.GOOGLE -> {
                signInWithGoogle()
            }
            AuthSocialCardInfo.GITHUB -> {
                authSocialWebViewFragment.showIfNotExists(childFragmentManager, AuthSocialWebViewFragment.TAG)
            }
            AuthSocialCardInfo.JETBRAINS -> {
                authSocialWebViewFragment.showIfNotExists(childFragmentManager, AuthSocialWebViewFragment.TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authMaterialCardViewsAdapter.items = listOf(
            AuthSocialCardInfo.JETBRAINS,
            AuthSocialCardInfo.GOOGLE,
            AuthSocialCardInfo.GITHUB
        )
        viewBinding.authButtonsRecyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.authButtonsRecyclerView.adapter = authMaterialCardViewsAdapter

        viewBinding.signInWithEmailMaterialButton.setOnClickListener {
            authSocialViewModel.onNewMessage(AuthSocialFeature.Message.ClickedContinueWithEmailEventMessage)
            requireRouter().navigateTo(AuthEmailScreen)
        }

        authSocialViewModel.onNewMessage(AuthSocialFeature.Message.ViewedEventMessage)
    }

    override fun onAction(action: AuthSocialFeature.Action.ViewAction) {
        when (action) {
            is AuthSocialFeature.Action.ViewAction.CompleteAuthFlow -> {
                val breadcrumb = Breadcrumb().apply {
                    category = SentryBreadcrumbKeyValues.CATEGORY_AUTH_SOCIAL
                    message = "Signed in with ${currentSocialAuthProvider?.title ?: "None"}"
                    level = SentryLevel.INFO
                }
                Sentry.addBreadcrumb(breadcrumb)

                (parentFragment as? AuthFlow)?.onAuthSuccess(action.isNewUser)
            }
            is AuthSocialFeature.Action.ViewAction.ShowAuthError -> {
                logSignInErrorToSentry(action.socialError, action.originalError,)
                view?.snackbar(message = authSocialErrorMapper.getAuthSocialErrorText(action.socialError), Snackbar.LENGTH_LONG)
            }
        }
    }

    override fun render(state: AuthSocialFeature.State) {
        if (state is AuthSocialFeature.State.Loading) {
            loadingProgressDialogFragment.showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            loadingProgressDialogFragment.dismissIfExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        }
    }

    private fun signInWithGoogle() {
        val serverClientId = BuildConfig.SERVER_CLIENT_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL), Scope(Scopes.PROFILE))
            .requestServerAuthCode(serverClientId)
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        signInWithGoogleCallback.launch(signInIntent)
    }

    override fun onError(error: AuthSocialError, originalError: Throwable?) {
        if (originalError != null) {
            logSignInErrorToSentry(error, originalError)
        }
        view?.snackbar(message = authSocialErrorMapper.getAuthSocialErrorText(error), Snackbar.LENGTH_LONG)
    }

    override fun onSuccess(authCode: String, provider: SocialAuthProvider) {
        authSocialViewModel.onNewMessage(AuthSocialFeature.Message.AuthWithSocial(authCode = authCode, socialAuthProvider = provider))
    }

    private fun logSignInErrorToSentry(socialError: AuthSocialError?, originalError: Throwable) {
        val breadcrumb = Breadcrumb().apply {
            category = SentryBreadcrumbKeyValues.CATEGORY_AUTH_SOCIAL
            message = "Sign in failed with ${currentSocialAuthProvider?.title ?: "None"}"
            level = SentryLevel.ERROR
        }
        Sentry.addBreadcrumb(breadcrumb)

        if (socialError != null) {
            Sentry.captureMessage("AuthSocial: $socialError, $originalError", SentryLevel.ERROR)
        } else {
            Sentry.captureMessage("AuthSocial: $originalError", SentryLevel.ERROR)
        }
    }
}
