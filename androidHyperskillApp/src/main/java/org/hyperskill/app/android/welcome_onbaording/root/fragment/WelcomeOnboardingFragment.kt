package org.hyperskill.app.android.welcome_onbaording.root.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.notification_onboarding.navigation.NotificationsOnboardingScreen
import org.hyperskill.app.android.welcome_onbaording.entry_point.navigation.WelcomeOnboardingEntryPointScreen
import org.hyperskill.app.android.welcome_onbaording.finish.navigation.WelcomeOnboardingFinishScreen
import org.hyperskill.app.android.welcome_onbaording.language.navigation.WelcomeOnboardingChooseProgrammingLanguageScreen
import org.hyperskill.app.android.welcome_onbaording.questionnaire.navigation.WelcomeQuestionnaireScreen
import org.hyperskill.app.android.welcome_onbaording.root.model.WelcomeOnboardingCompleteResult
import org.hyperskill.app.android.welcome_onbaording.root.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.track.navigation.WelcomeOnboardingTrackDetailsScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireType
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.root.presentation.WelcomeOnboardingViewModel
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class WelcomeOnboardingFragment : FlowFragment(), WelcomeOnboardingHost {

    companion object {
        const val WELCOME_ONBOARDING_COMPLETED = "WELCOME_ONBOARDING_COMPLETED"
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
        (navigator as? NestedAppNavigator)
            ?.interceptors
            ?.add(WelcomeOnboardingTransactionInterceptor)
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformWelcomeOnboardingComponent(params)
                .reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeOnboardingViewModel
            .state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(state: WelcomeOnboardingFeature.State) {
        if (state.isNextLearningActivityLoadingShown) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
        }
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

    @SuppressLint("InlinedApi")
    override fun onTrackSelected(track: WelcomeOnboardingTrack) {
        welcomeOnboardingViewModel.onTrackSelected(
            track,
            isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    override fun onNotificationOnboardingCompleted() {
        welcomeOnboardingViewModel.onNotificationPermissionCompleted()
    }

    override fun onFinishScreenViewed() {
        welcomeOnboardingViewModel.onFinishOnboardingShowed()
    }

    override fun onFinishClicked() {
        welcomeOnboardingViewModel.onFinishClicked()
    }

    override fun onStartOnboardingViewed() {
        welcomeOnboardingViewModel.onStartOnboardingViewed()
    }

    override fun onUserQuestionnaireViewed(questionnaireType: WelcomeQuestionnaireType) {
        welcomeOnboardingViewModel.onUserQuestionnaireViewed(questionnaireType)
    }

    override fun onSelectProgrammingLanguageViewed() {
        welcomeOnboardingViewModel.onSelectProgrammingLanguageViewed()
    }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NavigateTo.StartScreen ->
                router.newRootScreen(WelcomeOnboardingEntryPointScreen)
            is ViewAction.NavigateTo.WelcomeOnboardingQuestionnaire ->
                router.newRootScreen(WelcomeQuestionnaireScreen(action.type))
            ViewAction.NavigateTo.ChooseProgrammingLanguage ->
                router.newRootScreen(WelcomeOnboardingChooseProgrammingLanguageScreen)
            is ViewAction.NavigateTo.TrackDetails ->
                router.navigateTo(WelcomeOnboardingTrackDetailsScreen(action.track))
            ViewAction.NavigateTo.NotificationOnboarding ->
                router.newRootScreen(NotificationsOnboardingScreen)
            is ViewAction.NavigateTo.OnboardingFinish ->
                router.newRootScreen(WelcomeOnboardingFinishScreen(action.selectedTrack))
            is ViewAction.CompleteWelcomeOnboarding -> {
                val stepRoute = action.stepRoute
                requireAppRouter().sendResult(
                    WELCOME_ONBOARDING_COMPLETED,
                    if (stepRoute == null) {
                        WelcomeOnboardingCompleteResult.Empty
                    } else {
                        WelcomeOnboardingCompleteResult.StepRoute(stepRoute)
                    }
                )
            }
        }
    }
}