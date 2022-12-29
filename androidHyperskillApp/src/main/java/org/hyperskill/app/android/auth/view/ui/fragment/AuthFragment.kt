package org.hyperskill.app.android.auth.view.ui.fragment

import android.os.Bundle
import org.hyperskill.app.android.auth.view.ui.navigation.AuthFlow
import org.hyperskill.app.android.auth.view.ui.navigation.AuthSocialScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.profile.domain.model.Profile
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class AuthFragment : FlowFragment(), AuthFlow {
    companion object {
        const val AUTH_SUCCESS = "auth_success"

        fun newInstance(isInSignUpMode: Boolean): AuthFragment =
            AuthFragment()
                .apply {
                    this.isInSignUpMode = isInSignUpMode
                }
    }

    // TODO: read for more info https://vyahhi.myjetbrains.com/youtrack/issue/ALTAPPS-505
    // Based on this flag we should hide/show sign with email button and change title to Sign up
    private var isInSignUpMode: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initNavigation()
        }
    }

    override fun onAuthSuccess(profile: Profile) {
        requireAppRouter().sendResult(AUTH_SUCCESS, profile)
    }

    private fun initNavigation() {
        router.newRootScreen(AuthSocialScreen)
    }
}