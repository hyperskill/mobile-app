package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.app.presentation.redux.container.ReduxView

class AuthSocialFragment :
    Fragment(R.layout.fragment_auth_social),
    ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction> {

    private val viewBinding by viewBinding(FragmentAuthSocialBinding::bind)
    private lateinit var viewStateDelegate: ViewStateDelegate<AuthFeature.State>

    private val signInWithGoogleCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
        } catch (e: ApiException) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = ViewStateDelegate()

        viewBinding.signInSocialLoginButton.setOnClickListener {
            viewStateDelegate.switchState(AuthFeature.State.Loading)
            signInWithGoogle()
        }
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {}

    override fun render(state: AuthFeature.State) {}

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
}
