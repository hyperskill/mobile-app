package org.hyperskill.app.android.home.view.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.config.BuildKonfig

class PlaceholderNewUserFragment : Fragment(R.layout.fragment_placeholder_new_user_screen) {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private lateinit var authSharedFlow: MutableSharedFlow<UserDeauthorized>

    private val viewBinding: FragmentPlaceholderNewUserScreenBinding by viewBinding(
        FragmentPlaceholderNewUserScreenBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        authSharedFlow = HyperskillApp.graph().networkComponent.authorizationFlow
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BuildKonfig.BASE_URL)
            startActivity(intent)
        }

        viewBinding.placeholderSignInButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                authSharedFlow.emit(UserDeauthorized)
                requireRouter().newRootScreen(AuthScreen)
            }
        }
    }
}