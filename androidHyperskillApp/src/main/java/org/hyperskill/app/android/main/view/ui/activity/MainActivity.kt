package org.hyperskill.app.android.main.view.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
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
import org.hyperskill.app.android.core.view.ui.fragment.ReduxViewLifecycleObserver
import org.hyperskill.app.android.core.view.ui.navigation.AppNavigationContainer
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.main.view.ui.OrientationHost
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.click_handling.delegate.NotificationClickHandlingDelegate
import org.hyperskill.app.android.notification.model.ClickedNotificationData
import org.hyperskill.app.android.notification.model.DailyStudyReminderClickedData
import org.hyperskill.app.android.notification.model.DefaultNotificationClickedData
import org.hyperskill.app.android.notification.model.PushNotificationClickedData
import org.hyperskill.app.android.onboarding.navigation.OnboardingScreen
import org.hyperskill.app.android.profile_settings.view.mapper.ThemeMapper
import org.hyperskill.app.android.streak_recovery.view.delegate.StreakRecoveryViewActionDelegate
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import org.hyperskill.app.notification.local.domain.analytic.NotificationDailyStudyReminderClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.router.observeResult
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class MainActivity :
    AppCompatActivity(),
    AppNavigationContainer,
    ReduxView<AppFeature.State, AppFeature.Action.ViewAction>,
    OrientationHost {

    companion object {
        private const val INITIAL_ORIENTATION_KEY = "INITIAL_ORIENTATION_KEY"
        private const val WAS_ORIENTATION_CHANGED_KEY = "WAS_ORIENTATION_CHANGED_KEY"
        private const val DEFAULT_SAVED_ORIENTATION = Int.MIN_VALUE
    }

    private var wasOrientationChanged: Boolean = false
    private var initialOrientation: Int? = null

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

    private lateinit var profileSettings: ProfileSettings

    private lateinit var analyticInteractor: AnalyticInteractor

    private val notificationInteractor =
        HyperskillApp.graph().platformLocalNotificationComponent.notificationInteractor

    override val navigator by lazy {
        NestedAppNavigator(
            this,
            R.id.mainNavigationContainer,
            supportFragmentManager
        )
    }

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

        viewBinding.mainError.tryAgain.setOnClickListener {
            mainViewModel.onNewMessage(
                AppFeature.Message.Initialize(
                    pushNotificationData = (parseClickedNotificationData(intent) as? PushNotificationClickedData)?.data,
                    forceUpdate = true
                )
            )
        }

        startupViewModel(intent)

        lifecycleScope.launch {
            router
                .observeResult(AuthFragment.AUTH_SUCCESS)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    val profile = (it as? Profile) ?: return@collectLatest
                    mainViewModel.onNewMessage(AppFeature.Message.UserAuthorized(profile))
                }
        }

        setupOrientation(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(ThemeMapper.getAppCompatDelegate(profileSettings.theme))

        logNotificationAvailability()
    }

    private fun injectManual() {
        viewModelFactory = HyperskillApp.graph().platformMainComponent.reduxViewModelFactory
        profileSettings =
            HyperskillApp.graph().buildProfileSettingsComponent().profileSettingsInteractor.getProfileSettings()
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

    private fun setupOrientation(savedInstanceState: Bundle?) {
        val wasOrientationChanged =
            savedInstanceState?.getBoolean(WAS_ORIENTATION_CHANGED_KEY, false) ?: false
        this.wasOrientationChanged = wasOrientationChanged
        if (!resources.getBoolean(R.bool.is_tablet) && !wasOrientationChanged) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        val savedOrientation =
            savedInstanceState
                ?.getInt(INITIAL_ORIENTATION_KEY, DEFAULT_SAVED_ORIENTATION)?.let { savedOrientation ->
                    if (savedOrientation == DEFAULT_SAVED_ORIENTATION) {
                        null
                    } else {
                        savedOrientation
                    }
                }
        this.initialOrientation = savedOrientation ?: requestedOrientation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        initialOrientation?.let {
            outState.putInt(INITIAL_ORIENTATION_KEY, it)
        }
        outState.putBoolean(WAS_ORIENTATION_CHANGED_KEY, wasOrientationChanged)
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

    override fun requestOrientation(orientation: Int) {
        if (requestedOrientation != orientation) {
            wasOrientationChanged = true
            requestedOrientation = orientation
        }
    }

    override fun backToInitialOrientation() {
        if (!isChangingConfigurations) {
            wasOrientationChanged = false
            initialOrientation?.let {
                requestedOrientation = it
            }
        }
    }

    override fun onAction(action: AppFeature.Action.ViewAction) {
        when (action) {
            is AppFeature.Action.ViewAction.NavigateTo.OnboardingScreen ->
                router.newRootScreen(OnboardingScreen)
            is AppFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                router.newRootScreen(AuthScreen())
            is AppFeature.Action.ViewAction.NavigateTo.HomeScreen ->
                router.newRootScreen(MainScreen())
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
        }
    }

    override fun render(state: AppFeature.State) {
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