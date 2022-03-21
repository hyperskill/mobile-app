package org.hyperskill.app.android.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentAuthSocialBinding

class AuthSocialFragment : Fragment(R.layout.fragment_auth_social) {

    private lateinit var viewBinding: FragmentAuthSocialBinding
    private lateinit var viewModel: AuthSocialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthSocialViewModel::class.java]
        viewBinding = FragmentAuthSocialBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    renderViewState(viewState)
                }
            }
        }
    }

    private fun renderViewState(viewState: AuthSocialViewModel.ViewState) {
        when (viewState) {
            is AuthSocialViewModel.ViewState.Loading -> {
                viewBinding.signInSocialProgressIndicator.visibility = View.VISIBLE
            }
            is AuthSocialViewModel.ViewState.Idle -> {
                viewBinding.signInSocialProgressIndicator.isVisible = false
                viewBinding.signInSocialProgressIndicator.visibility = View.INVISIBLE
            }
            is AuthSocialViewModel.ViewState.Error -> {
                viewBinding.signInSocialProgressIndicator.isVisible = false
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