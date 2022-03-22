package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding
import org.hyperskill.app.android.presentation.view.AuthSocialView
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

class AuthSocialFragment : Fragment(R.layout.fragment_auth_social), AuthSocialView {

    private lateinit var viewBinding: FragmentAuthSocialBinding
    private lateinit var viewStateDelegate: ViewStateDelegate<AuthSocialView.State>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = ViewStateDelegate()
        viewStateDelegate.addState<AuthSocialView.State.Idle>()
        viewStateDelegate.addState<AuthSocialView.State.Loading>()
        viewStateDelegate.addState<AuthSocialView.State.NetworkError>()
    }

    override fun setState(state: AuthSocialView.State) {
        viewStateDelegate.switchState(state)
        when (state) {
            is AuthSocialView.State.Loading -> {
                viewBinding.signInSocialProgressIndicator.visibility = View.VISIBLE
            }
            is AuthSocialView.State.Idle -> {
                viewBinding.signInSocialProgressIndicator.visibility = View.INVISIBLE
            }
            is AuthSocialView.State.NetworkError -> {
                viewBinding.signInSocialProgressIndicator.visibility = View.INVISIBLE
                // TODO: make error text
                view?.let {
                    Snackbar.make(it, "Login error", Snackbar.LENGTH_LONG)
                        .setAction("Retry") {
                        }
                        .show()
                }
            }
//            is AuthSocialViewModel.ViewState.Data -> {
//                viewBinding.signInSocialProgressIndicator.isVisible = false
//            }
        }
    }
}