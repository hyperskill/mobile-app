package org.hyperskill.app.android.welcome_onbaording.language.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.language.ui.WelcomeOnboardingChooseProgrammingLanguage
import org.hyperskill.app.android.welcome_onbaording.root.model.WelcomeOnboardingHost
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage

class WelcomeOnboardingChooseProgrammingLanguageFragment : Fragment() {
    companion object {
        fun newInstance(): WelcomeOnboardingChooseProgrammingLanguageFragment =
            WelcomeOnboardingChooseProgrammingLanguageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    WelcomeOnboardingChooseProgrammingLanguage(onLangClick = ::onLanguageSelected)
                }
            }
        }

    private fun onLanguageSelected(language: WelcomeOnboardingProgrammingLanguage) {
        (parentFragment as? WelcomeOnboardingHost)?.onProgrammingLanguageSelected(language)
    }
}