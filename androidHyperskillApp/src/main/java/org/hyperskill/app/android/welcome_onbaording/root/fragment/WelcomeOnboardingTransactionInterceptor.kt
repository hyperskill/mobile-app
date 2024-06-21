package org.hyperskill.app.android.welcome_onbaording.root.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.R
import ru.nobird.android.view.navigation.navigator.FragmentTransactionInterceptor

object WelcomeOnboardingTransactionInterceptor : FragmentTransactionInterceptor {
    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        if (currentFragment != null) {
            fragmentTransaction.setCustomAnimations(
                /* enter = */ R.anim.navigation_slide_in,
                /* exit = */ R.anim.fade_out
            )
        }
    }
}