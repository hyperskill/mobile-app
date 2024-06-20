package org.hyperskill.app.android.welcome_onbaording.questionnaire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.welcome_onbaording.questionnaire.ui.WelcomeQuestionnaire
import org.hyperskill.app.android.welcome_onbaording.root.model.WelcomeOnboardingHost
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.questionnaire.view.WelcomeQuestionnaireViewState
import org.hyperskill.app.welcome_onboarding.questionnaire.view.WelcomeQuestionnaireViewStateMapper
import ru.nobird.android.view.base.ui.extension.argument

class WelcomeQuestionnaireFragment : Fragment() {
    companion object {
        fun newInstance(type: WelcomeQuestionnaireType): WelcomeQuestionnaireFragment =
            WelcomeQuestionnaireFragment().apply {
                this.type = type
            }
    }

    private var type: WelcomeQuestionnaireType by argument()

    private var viewStateMapper: WelcomeQuestionnaireViewStateMapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewStateMapper =
            HyperskillApp
                .graph()
                .buildWelcomeQuestionnaireComponent()
                .welcomeQuestionnaireViewStateMapper
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                val viewState: WelcomeQuestionnaireViewState = remember {
                    requireNotNull(viewStateMapper).mapQuestionnaireTypeToViewState(type)
                }
                OnComposableShownFirstTime(key = Unit, block = ::onShow)
                HyperskillTheme {
                    WelcomeQuestionnaire(viewState, onItemClick = ::onItemClick)
                }
            }
        }

    private fun onItemClick(itemType: WelcomeQuestionnaireItemType) {
        (parentFragment as? WelcomeOnboardingHost)?.onQuestionnaireItemClicked(
            questionnaireType = type,
            itemType = itemType
        )
    }

    private fun onShow() {
        (parentFragment as? WelcomeOnboardingHost)?.onUserQuestionnaireViewed(type)
    }
}