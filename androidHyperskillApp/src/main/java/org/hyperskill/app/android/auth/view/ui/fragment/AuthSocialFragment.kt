package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.util.Log
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
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.presentation.AuthSocialViewModel
import org.hyperskill.app.android.auth.view.ui.adapter.delegates.AuthSocialAdapterDelegate
import org.hyperskill.app.android.auth.view.ui.model.AuthSocialCardInfo
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.auth.view.ui.navigation.AuthEmailScreen
import org.hyperskill.app.android.auth.view.ui.navigation.AuthFlow
import org.hyperskill.app.android.core.view.ui.dialog.dismissIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import org.hyperskill.app.core.view.mapper.ResourceProvider
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class AuthSocialFragment :
    Fragment(R.layout.fragment_auth_social),
    ReduxView<AuthSocialFeature.State, AuthSocialFeature.Action.ViewAction>,
    AuthSocialWebViewFragment.Callback {

    companion object {
        fun newInstance(): AuthSocialFragment =
            AuthSocialFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var resourceProvider: ResourceProvider

    @Inject
    internal lateinit var authSocialErrorMapper: AuthSocialErrorMapper

    private val authSocialViewModel: AuthSocialViewModel by reduxViewModel(this) { viewModelFactory }

    private val viewBinding by viewBinding(FragmentAuthSocialBinding::bind)
    private val authMaterialCardViewsAdapter: DefaultDelegateAdapter<AuthSocialCardInfo> = DefaultDelegateAdapter()

    private val loadingProgressDialogFragment: DialogFragment =
        LoadingProgressDialogFragment.newInstance()

    private val signInWithGoogleCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            onSuccess(authCode!!, SocialAuthProvider.GOOGLE)
        } catch (e: ApiException) {
            if (e.statusCode == CommonStatusCodes.NETWORK_ERROR) {
                view?.snackbar(message = resourceProvider.getString(SharedResources.strings.connection_error), Snackbar.LENGTH_LONG)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        authMaterialCardViewsAdapter += AuthSocialAdapterDelegate(::onSocialClickListener)

    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .authSocialComponentBuilder()
            .build()
            .inject(this)
    }

    private fun onSocialClickListener(social: AuthSocialCardInfo) {
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
            requireRouter().navigateTo(AuthEmailScreen)
        }
    }

    override fun onAction(action: AuthSocialFeature.Action.ViewAction) {
        when (action) {
            is AuthSocialFeature.Action.ViewAction.CompleteAuthFlow -> {
                (parentFragment as? AuthFlow)?.onAuthSuccess()
            }
            is AuthSocialFeature.Action.ViewAction.ShowAuthError -> {
                Log.d("CALLBACK", "SHOW ERROR")
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

    override fun onError(error: AuthSocialError) {
        authSocialViewModel.onNewMessage(AuthSocialFeature.Message.AuthFailure(error))
        view?.snackbar(message = authSocialErrorMapper.getAuthSocialErrorText(error), Snackbar.LENGTH_LONG)
    }

    override fun onSuccess(authCode: String, provider: SocialAuthProvider) {
        authSocialViewModel.onNewMessage(AuthSocialFeature.Message.AuthWithSocial(authCode, provider))
    }
}
