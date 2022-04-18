package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.presentation.AuthEmailViewModel
import org.hyperskill.app.android.auth.view.ui.screen.AuthSocialScreen
import org.hyperskill.app.android.databinding.FragmentAuthEmailBinding
import org.hyperskill.app.android.main.view.ui.activity.MainActivity
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class AuthEmailFragment :
    Fragment(R.layout.fragment_auth_email),
    ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction> {

    companion object {
        fun newInstance(): AuthEmailFragment =
            AuthEmailFragment()
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val authEmailViewModel: AuthEmailViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewBinding by viewBinding(FragmentAuthEmailBinding::bind)
    private lateinit var viewStateDelegate: ViewStateDelegate<AuthFeature.State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        viewBinding.signInWithSocialMaterialButton.setOnClickListener {
            (requireActivity() as MainActivity).router.backTo(AuthSocialScreen)
        }
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .authEmailComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {
        when (action) {
            is AuthFeature.Action.ViewAction.ShowAuthError -> {
                showError(action.errorMsg)
            }
        }
    }

    override fun render(state: AuthFeature.State) {}

    private fun showError(msg: String) {
        viewBinding.emailTextInputLayout.error = " "
        viewBinding.passwordTextInputLayout.error = " "
        viewBinding.authEmailErrorMsgTextView.apply {
            visibility = View.VISIBLE
            text = msg
        }
    }

    private fun hideError() {
        viewBinding.emailTextInputLayout.error = null
        viewBinding.passwordTextInputLayout.error = null
        viewBinding.authEmailErrorMsgTextView.apply {
            visibility = View.GONE
            text = null
        }
    }
}