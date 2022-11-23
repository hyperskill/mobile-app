package org.hyperskill.app.android.auth.view.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthFlow
import org.hyperskill.app.android.auth.view.ui.navigation.AuthSocialScreen
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentAuthEmailBinding
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.presentation.AuthCredentialsViewModel
import org.hyperskill.app.auth.view.mapper.AuthCredentialsErrorMapper
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.addKeyboardVisibilityListener
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class AuthCredentialsFragment :
    Fragment(R.layout.fragment_auth_email),
    ReduxView<AuthCredentialsFeature.State, AuthCredentialsFeature.Action.ViewAction> {

    companion object {
        fun newInstance(): AuthCredentialsFragment =
            AuthCredentialsFragment()
    }

    private lateinit var authCredentialsErrorMapper: AuthCredentialsErrorMapper
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewStateDelegate: ViewStateDelegate<AuthCredentialsFeature.FormState> = ViewStateDelegate()
    private val authCredentialsViewModel: AuthCredentialsViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewBinding by viewBinding(FragmentAuthEmailBinding::bind)

    private val loadingProgressDialogFragment: DialogFragment =
        LoadingProgressDialogFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.emailEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        viewBinding.emailEditText.doAfterTextChanged {
            authCredentialsViewModel.onNewMessage(
                AuthCredentialsFeature.Message.AuthEditing(
                    viewBinding.emailEditText.text.toString(),
                    viewBinding.passwordEditText.text.toString()
                )
            )
        }
        viewBinding.passwordEditText.doAfterTextChanged {
            authCredentialsViewModel.onNewMessage(
                AuthCredentialsFeature.Message.AuthEditing(
                    viewBinding.emailEditText.text.toString(),
                    viewBinding.passwordEditText.text.toString()
                )
            )
        }
        viewBinding.passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.SubmitFormClicked)
                handled = true
            }
            handled
        }
        viewBinding.signInWithEmailMaterialButton.setOnClickListener {
            authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.ClickedSignInEventMessage)
            authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.SubmitFormClicked)
        }
        viewBinding.signInWithEmailResetPasswordTextButton.setOnClickListener {
            authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.ClickedResetPasswordEventMessage)
        }
        viewBinding.signInWithSocialMaterialButton.setOnClickListener {
            authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.ClickedContinueWithSocialEventMessage)
            requireRouter().backTo(AuthSocialScreen)
        }

        viewBinding.signInWithEmailResetPasswordTextButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = HyperskillUrlBuilder.build(HyperskillUrlPath.ResetPassword())
            intent.data = Uri.parse(url.toString())

            startActivity(intent)
        }

        viewBinding.root.addKeyboardVisibilityListener { isVisible ->
            if (!isAdded) return@addKeyboardVisibilityListener
            viewBinding.signInHyperskillLogoShapeableImageView.isVisible = !isVisible
            viewBinding.signInToTextView.isVisible = !isVisible
        }

        authCredentialsViewModel.onNewMessage(AuthCredentialsFeature.Message.ViewedEventMessage)
    }

    private fun injectComponent() {
        val authCredentialsComponent = HyperskillApp.graph().buildAuthCredentialsComponent()
        val platformAuthComponent =
            HyperskillApp.graph().buildPlatformAuthCredentialsComponent(authCredentialsComponent)
        authCredentialsErrorMapper = authCredentialsComponent.authCredentialsErrorMapper
        viewModelFactory = platformAuthComponent.reduxViewModelFactory
    }

    override fun onAction(action: AuthCredentialsFeature.Action.ViewAction) {
        when (action) {
            is AuthCredentialsFeature.Action.ViewAction.CompleteAuthFlow ->
                (parentFragment as? AuthFlow)?.onAuthSuccess(action.isNewUser)
        }
    }

    override fun render(state: AuthCredentialsFeature.State) {
        viewStateDelegate.switchState(state.formState)
        renderFormState(state.formState)
        viewBinding.emailEditText.setTextIfChanged(state.email)
        viewBinding.passwordEditText.setTextIfChanged(state.password)
    }

    private fun renderFormState(formState: AuthCredentialsFeature.FormState) {
        viewStateDelegate.switchState(formState)
        if (formState is AuthCredentialsFeature.FormState.Loading) {
            loadingProgressDialogFragment.showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            loadingProgressDialogFragment.dismissIfExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        }

        if (formState is AuthCredentialsFeature.FormState.Error) {
            showError(authCredentialsErrorMapper.getAuthCredentialsErrorText(formState.credentialsError))
        } else {
            hideError()
        }
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<AuthCredentialsFeature.FormState.Editing>(viewBinding.authInputContainer)
        viewStateDelegate.addState<AuthCredentialsFeature.FormState.Loading>(viewBinding.authInputContainer)
        viewStateDelegate.addState<AuthCredentialsFeature.FormState.Error>(
            viewBinding.authInputContainer,
            viewBinding.authEmailErrorMsgTextView
        )
        viewStateDelegate.addState<AuthCredentialsFeature.FormState.Authenticated>(viewBinding.authInputContainer)
    }

    private fun showError(message: String) {
        viewBinding.emailTextInputLayout.error = " "
        viewBinding.passwordTextInputLayout.error = " "
        viewBinding.authEmailErrorMsgTextView.text = message
    }

    private fun hideError() {
        viewBinding.emailTextInputLayout.error = null
        viewBinding.passwordTextInputLayout.error = null
        viewBinding.authEmailErrorMsgTextView.text = null
    }
}