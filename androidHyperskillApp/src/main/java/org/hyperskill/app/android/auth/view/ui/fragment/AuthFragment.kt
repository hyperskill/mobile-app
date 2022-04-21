package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import org.hyperskill.app.android.auth.view.ui.screen.AuthSocialScreen
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class AuthFragment : FlowFragment() {
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

    private fun initNavigation() {
        router.newRootScreen(AuthSocialScreen)
    }
}