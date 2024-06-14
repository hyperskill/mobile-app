package org.hyperskill.app.android.welcome_onbaording.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome_onbaording.fragment.WelcomeQuestionnaireFragment
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeQuestionnaireType

class WelcomeQuestionnaireScreen(
    private val type: WelcomeQuestionnaireType
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeQuestionnaireFragment.newInstance(type)
}