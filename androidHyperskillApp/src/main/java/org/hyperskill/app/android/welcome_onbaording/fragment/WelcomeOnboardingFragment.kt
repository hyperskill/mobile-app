package org.hyperskill.app.android.welcome_onbaording.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.welcome_onbaording.language.navigation.WelcomeOnboardingChooseProgrammingLanguageScreen
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeOnboardingEntryPointScreen
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeQuestionnaireScreen
import org.hyperskill.app.android.welcome_onbaording.track.navigation.WelcomeOnboardingTrackDetailsScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingViewModel
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class WelcomeOnboardingFragment : FlowFragment(), WelcomeOnboardingHost {
    companion object {
        fun newInstance(params: WelcomeOnboardingFeatureParams): WelcomeOnboardingFragment =
            WelcomeOnboardingFragment().apply {
                this.params = params
            }
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val welcomeOnboardingViewModel: WelcomeOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private var params: WelcomeOnboardingFeatureParams by argument(WelcomeOnboardingFeatureParams.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        welcomeOnboardingViewModel.handleActions(this, ::onAction)
        if (savedInstanceState == null) {
            initNavigation(welcomeOnboardingViewModel.state.value)
        }
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformWelcomeOnboardingComponent(params)
                .reduxViewModelFactory
    }

    private fun initNavigation(state: WelcomeOnboardingFeature.State) {
        val screen = when (state.initialStep) {
            WelcomeOnboardingStartScreen.START_SCREEN -> WelcomeOnboardingEntryPointScreen
            WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING -> TODO()
        }
        router.newRootScreen(screen)
    }

    override fun onStartClick() {
        welcomeOnboardingViewModel.onStartClick()
    }

    override fun onQuestionnaireItemClicked(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    ) {
        welcomeOnboardingViewModel.onQuestionnaireItemClicked(questionnaireType, itemType)
    }

    override fun onProgrammingLanguageSelected(language: WelcomeOnboardingProgrammingLanguage) {
        welcomeOnboardingViewModel.onLanguageSelected(language)
    }

    override fun onTrackSelected(track: WelcomeOnboardingTrack) {
        welcomeOnboardingViewModel.onTrackSelected(track)
    }

    private fun onAction(action: ViewAction) {
        when (action) {
            is ViewAction.NavigateTo.WelcomeOnboardingQuestionnaire ->
                router.newRootScreen(WelcomeQuestionnaireScreen(action.type))
            ViewAction.NavigateTo.ChooseProgrammingLanguage ->
                router.newRootScreen(WelcomeOnboardingChooseProgrammingLanguageScreen)
            is ViewAction.NavigateTo.TrackDetails ->
                router.navigateTo(WelcomeOnboardingTrackDetailsScreen(action.track))
        }
    }
}