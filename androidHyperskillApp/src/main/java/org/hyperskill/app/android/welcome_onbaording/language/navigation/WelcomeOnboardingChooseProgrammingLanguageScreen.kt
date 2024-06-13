package org.hyperskill.app.android.welcome_onbaording.language.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.language.fragment.WelcomeOnboardingChooseProgrammingLanguageFragment

object WelcomeOnboardingChooseProgrammingLanguageScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeOnboardingChooseProgrammingLanguageFragment.newInstance()
}