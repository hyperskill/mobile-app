package org.hyperskill.app.android.auth.view.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.app.presentation.redux.container.ReduxView


class AuthSocialFragment
    : Fragment(R.layout.fragment_auth_social),
    ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction>
{

    private lateinit var viewBinding: FragmentAuthSocialBinding
    private lateinit var viewStateDelegate: ViewStateDelegate<AuthFeature.State>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = ViewStateDelegate()
        viewStateDelegate.addState<AuthFeature.State.Idle>()
        viewStateDelegate.addState<AuthFeature.State.Loading>(viewBinding.signInSocialProgressIndicator)

        viewBinding.signInSocialLoginButton.setOnClickListener {
            viewStateDelegate.switchState(AuthFeature.State.Loading)
            signInWithGoogle(requireActivity())
        }
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {}
    override fun render(state: AuthFeature.State) {
        if (state is AuthFeature.State.NetworkError) {
            // TODO: add text and action depending on error
            Snackbar.make(requireView(), "Login error", Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                }
                .show()
        }
    }

    private fun signInWithGoogle(activity: Activity) {
        //TODO: change to server client ID
        val serverClientId = "CHANGE TO SERVER CLIENT ID"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestServerAuthCode(serverClientId)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        val signInIntent = mGoogleSignInClient.signInIntent
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)

        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            viewStateDelegate.switchState(AuthFeature.State.Authenticated(authCode ?: ""))
        } catch (e: ApiException) {
            viewStateDelegate.switchState(AuthFeature.State.NetworkError)
        }
    }
}
