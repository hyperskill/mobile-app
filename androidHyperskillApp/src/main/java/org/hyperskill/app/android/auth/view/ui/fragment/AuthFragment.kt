package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import org.hyperskill.app.android.auth.view.ui.navigation.AuthFlow
import org.hyperskill.app.android.auth.view.ui.navigation.AuthSocialScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class AuthFragment : FlowFragment(), AuthFlow {
    companion object {
        const val AUTH_SUCCESS = "auth_success"

        fun newInstance(): AuthFragment =
            AuthFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initNavigation()
        }
    }

    override fun onAuthSuccess() {
        requireAppRouter()?.sendResult(AUTH_SUCCESS, Unit)
    }

    private fun initNavigation() {
        router.newRootScreen(AuthSocialScreen)
    }
}