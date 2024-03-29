package org.hyperskill.app.android.users_questionnaire.onboarding.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.users_questionnaire.onboarding.fragment.UsersQuestionnaireOnboardingFragment

object UsersQuestionnaireOnboardingScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        UsersQuestionnaireOnboardingFragment.newInstance()
}