package org.hyperskill.app.android.main.view.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.fragment.AuthFragment
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.clarity.ClarityDelegate
import org.hyperskill.app.android.core.extensions.screenOrientation
import org.hyperskill.app.android.core.view.ui.fragment.ReduxViewLifecycleObserver
import org.hyperskill.app.android.core.view.ui.navigation.AppNavigationContainer
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.click_handling.delegate.NotificationClickHandlingDelegate
import org.hyperskill.app.android.notification.model.ClickedNotificationData
import org.hyperskill.app.android.notification.model.DailyStudyReminderClickedData
import org.hyperskill.app.android.notification.model.DefaultNotificationClickedData
import org.hyperskill.app.android.notification.model.PushNotificationClickedData
import org.hyperskill.app.android.paywall.fragment.PaywallFragment
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.step.view.navigation.StepScreen
import org.hyperskill.app.android.streak_recovery.view.delegate.StreakRecoveryViewActionDelegate
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.android.welcome.navigation.WelcomeScreen
import org.hyperskill.app.android.welcome_onbaording.fragment.WelcomeOnboardingFragment
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingCompleteResult
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeOnboardingScreen
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.local.domain.analytic.NotificationDailyStudyReminderClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.router.observeResult
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.container.ReduxView

class MainActivity :
    AppCompatActivity(),
    AppNavigationContainer,
    ReduxView<AppFeature.State, AppFeature.Action.ViewAction> {

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewStateDelegate: ViewStateDelegate<AppFeature.State> = ViewStateDelegate()

    /**
     * [reduxViewModel] delegate function can't be used, because it doesn't support [SavedStateHandle],
     * that's why standard [viewModels] function is used.
     */
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    private val localCicerone: Cicerone<Router> = Cicerone.create()
    override val router: Router = localCicerone.router

    private lateinit var analyticInteractor: AnalyticInteractor

    private val notificationInteractor =
        HyperskillApp.graph().platformLocalNotificationComponent.notificationInteractor

    private val clarityDelegate: ClarityDelegate =
        ClarityDelegate(HyperskillApp.graph().commonComponent.settings)

    override val navigator by lazy {
        NestedAppNavigator(
            this,
            R.id.mainNavigationContainer,
            supportFragmentManager
        )
    }

    private val onForegroundObserver =
        object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                mainViewModel.onNewMessage(AppFeature.Message.AppBecomesActive)
            }
        }

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        lifecycle.addObserver(
            ReduxViewLifecycleObserver(this, ::mainViewModel)
        )

        onBackPressedDispatcher
            .addCallback(navigator.onBackPressedCallback)

        super.onCreate(savedInstanceState)
        injectManual()
        navigator.invalidateOnBackPressedCallback()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViewStateDelegate()
        lifecycle.addObserver(onForegroundObserver)

        viewBinding.mainError.tryAgain.setOnClickListener {
            mainViewModel.onNewMessage(
                AppFeature.Message.Initialize(
                    pushNotificationData = (parseClickedNotificationData(intent) as? PushNotificationClickedData)?.data,
                    forceUpdate = true
                )
            )
        }

        startupViewModel(intent)

        observeAuthFlowSuccess()
        observeWelcomeOnboardingCompleted()
        observePaywallIsShownChanged()

        mainViewModel.logScreenOrientation(screenOrientation = resources.configuration.screenOrientation)

        clarityDelegate.init(applicationContext, savedStateRegistry)

        logNotificationAvailability()
    }

    private fun injectManual() {
        viewModelFactory = HyperskillApp.graph().platformMainComponent.reduxViewModelFactory
        analyticInteractor = HyperskillApp.graph().analyticComponent.analyticInteractor
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<AppFeature.State.Idle>()
        viewStateDelegate.addState<AppFeature.State.Loading>(viewBinding.mainProgress)
        viewStateDelegate.addState<AppFeature.State.Ready>(viewBinding.mainNavigationContainer)
        viewStateDelegate.addState<AppFeature.State.NetworkError>(viewBinding.mainError.root)
    }

    private fun startupViewModel(intent: Intent?) {
        if (intent != null) {
            handleIntent(intent) { clickedNotificationData ->
                mainViewModel.startup(
                    (clickedNotificationData as? PushNotificationClickedData)?.data
                )
            }
        } else {
            mainViewModel.startup()
        }
    }

    @SuppressLint("InlinedApi")
    private fun observeAuthFlowSuccess() {
        observeResult<Profile>(AuthFragment.AUTH_SUCCESS) { profile ->
            mainViewModel.onNewMessage(
                AppFeature.Message.UserAuthorized(
                    profile = profile,
                    isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            )
            clarityDelegate.setUserId(profile.id)
        }
    }

    private fun observePaywallIsShownChanged() {
        observeResult<Boolean>(PaywallFragment.PAYWALL_IS_SHOWN_CHANGED) {
            mainViewModel.onNewMessage(
                AppFeature.Message.IsPaywallShownChanged(it)
            )
        }
    }

    private fun observeWelcomeOnboardingCompleted() {
        observeResult<WelcomeOnboardingCompleteResult>(WelcomeOnboardingFragment.WELCOME_ONBOARDING_COMPLETED) {
            mainViewModel.onNewMessage(
                AppFeature.Message.WelcomeOnboardingCompleted(
                    when (it) {
                        WelcomeOnboardingCompleteResult.Empty -> null
                        is WelcomeOnboardingCompleteResult.StepRoute -> it.stepRoute
                    }
                )
            )
        }
    }

    private inline fun <reified T> observeResult(
        key: String,
        router: Router = this.router,
        crossinline onResult: (T) -> Unit
    ) {
        lifecycleScope.launch {
            router
                .observeResult(key)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    val result = it.safeCast<T>()
                    if (result == null) {
                        Log.e("MainActivity", "Can't cast result by key=$key.")
                    } else {
                        onResult(result)
                    }
                }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent) { clickedNotificationData ->
                if (clickedNotificationData is PushNotificationClickedData) {
                    mainViewModel.onNewMessage(
                        AppFeature.Message.NotificationClicked(clickedNotificationData.data)
                    )
                }
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        localCicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        localCicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onAction(action: AppFeature.Action.ViewAction) {
        when (action) {
            is AppFeature.Action.ViewAction.NavigateTo.WelcomeScreen ->
                router.newRootScreen(WelcomeScreen)
            is AppFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                router.newRootScreen(AuthScreen())
            is AppFeature.Action.ViewAction.NavigateTo.TrackSelectionScreen ->
                router.newRootScreen(
                    TrackSelectionListScreen(
                        TrackSelectionListParams(isNewUserMode = true)
                    )
                )
            is AppFeature.Action.ViewAction.StreakRecoveryViewAction ->
                StreakRecoveryViewActionDelegate.handleViewAction(
                    fragmentManager = supportFragmentManager,
                    rootView = viewBinding.root,
                    viewAction = action.viewAction
                )
            is AppFeature.Action.ViewAction.ClickedNotificationViewAction -> {
                when (val viewAction = action.viewAction) {
                    is NotificationClickHandlingFeature.Action.ViewAction.SetLoadingShowed -> {
                        viewBinding.mainProgress.isVisible = viewAction.isLoadingShowed
                    }
                    is NotificationClickHandlingFeature.Action.ViewAction.NavigateTo -> {
                        intent = null
                        NotificationClickHandlingDelegate.onNavigationViewAction(router, viewAction)
                    }
                    is NotificationClickHandlingFeature.Action.ViewAction.ShowEarnedBadgeModal -> {
                        NotificationClickHandlingDelegate.onShowEarnedBadgeModalViewAction(
                            showEarnedBadgeModal = viewAction,
                            fragmentManager = supportFragmentManager
                        )
                    }
                }
            }
            AppFeature.Action.ViewAction.NavigateTo.StudyPlan ->
                router.newRootScreen(MainScreen(Tabs.STUDY_PLAN))
            is AppFeature.Action.ViewAction.NavigateTo.Step ->
                router.newRootChain(
                    MainScreen(Tabs.STUDY_PLAN),
                    StepScreen(action.stepRoute)
                )
            is AppFeature.Action.ViewAction.NavigateTo.Paywall -> {
                if (supportFragmentManager.findFragmentByTag(PaywallScreen.TAG) == null) {
                    router.navigateTo(PaywallScreen(action.paywallTransitionSource))
                }
            }
            is AppFeature.Action.ViewAction.NavigateTo.StudyPlanWithPaywall ->
                router.newRootChain(
                    MainScreen(initialTab = Tabs.STUDY_PLAN),
                    PaywallScreen(action.paywallTransitionSource)
                )
            is AppFeature.Action.ViewAction.NavigateTo.WelcomeOnboarding -> {
                router.newRootScreen(
                    WelcomeOnboardingScreen(
                        WelcomeOnboardingFeatureParams(action.profile, action.isNotificationPermissionGranted)
                    )
                )
            }
        }
    }

    override fun render(state: AppFeature.State) {
        HyperskillApp.setIsLeaderboardTabEnabled(
            state.safeCast<AppFeature.State.Ready>()?.isMobileLeaderboardsEnabled ?: false
        )
        viewStateDelegate.switchState(state)
        when (state) {
            is AppFeature.State.Idle, AppFeature.State.Loading ->
                window.statusBarColor = ContextCompat.getColor(this, org.hyperskill.app.R.color.color_black_900)
            is AppFeature.State.Ready ->
                window.statusBarColor = resolveColorAttribute(com.google.android.material.R.attr.colorPrimaryVariant)
            else -> {
                // no op
            }
        }
    }

    private fun handleIntent(
        intent: Intent,
        handlePushNotificationData: (ClickedNotificationData?) -> Unit
    ) {
        val clickedNotificationData = parseClickedNotificationData(intent)
        handlePushNotificationData(clickedNotificationData)
        if (clickedNotificationData != null) {
            logNotificationClickedEvent(clickedNotificationData)
        }
    }

    private fun parseClickedNotificationData(intent: Intent?): ClickedNotificationData? =
        with(NotificationIntentBuilder) {
            intent?.getClickedNotificationData()
        }

    // TODO: move to the viewModel
    private fun logNotificationClickedEvent(
        clickedNotificationData: ClickedNotificationData
    ) {
        when (clickedNotificationData) {
            is DailyStudyReminderClickedData -> {
                lifecycleScope.launch {
                    analyticInteractor.logEvent(
                        NotificationDailyStudyReminderClickedHyperskillAnalyticEvent(
                            notificationId = clickedNotificationData.notificationId
                        )
                    )
                }
            }
            is PushNotificationClickedData,
            is DefaultNotificationClickedData -> {
                // no op
            }
        }
    }

    // TODO: move to the viewModel
    private fun logNotificationAvailability() {
        notificationInteractor.setNotificationsPermissionGranted(
            NotificationManagerCompat.from(this).areNotificationsEnabled()
        )
    }
}