package org.hyperskill.app.android.main.view.ui.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
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
import org.hyperskill.app.android.core.view.ui.navigation.AppNavigationContainer
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.model.ClickedNotificationData
import org.hyperskill.app.android.notification.model.DailyStudyReminderClickedData
import org.hyperskill.app.android.notification.model.DefaultNotificationClickedData
import org.hyperskill.app.android.onboarding.navigation.OnboardingScreen
import org.hyperskill.app.android.placeholder_new_user.navigation.PlaceholderNewUserScreen
import org.hyperskill.app.android.profile_settings.view.mapper.ThemeMapper
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.notification.domain.analytic.NotificationDailyStudyReminderClickedHyperskillAnalyticEvent
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile_settings.domain.model.ProfileSettings
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.router.observeResult
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class MainActivity :
    AppCompatActivity(),
    AppNavigationContainer,
    ReduxView<AppFeature.State, AppFeature.Action.ViewAction> {
    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewStateDelegate: ViewStateDelegate<AppFeature.State> = ViewStateDelegate()

    private val mainViewModelProvider: MainViewModel by reduxViewModel(this) { viewModelFactory }
    private val localCicerone: Cicerone<Router> = Cicerone.create()
    override val router: Router = localCicerone.router

    private lateinit var profileSettings: ProfileSettings

    private lateinit var analyticInteractor: AnalyticInteractor

    override val navigator by lazy {
        NestedAppNavigator(
            this,
            R.id.mainNavigationContainer,
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        onBackPressedDispatcher
            .addCallback(navigator.onBackPressedCallback)

        super.onCreate(savedInstanceState)
        injectManual()
        navigator.invalidateOnBackPressedCallback()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViewStateDelegate()
        mainViewModelProvider.onNewMessage(AppFeature.Message.Initialize(forceUpdate = false))

        viewBinding.mainError.tryAgain.setOnClickListener {
            mainViewModelProvider.onNewMessage(AppFeature.Message.Initialize(forceUpdate = true))
        }

        lifecycleScope.launch {
            router
                .observeResult(AuthFragment.AUTH_SUCCESS)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    val profile = (it as? Profile) ?: return@collectLatest
                    mainViewModelProvider.onNewMessage(AppFeature.Message.UserAuthorized(profile))
                }
        }

        if (!resources.getBoolean(R.bool.is_tablet)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        AppCompatDelegate.setDefaultNightMode(ThemeMapper.getAppCompatDelegate(profileSettings.theme))

        handleNewIntent(intent)
    }

    private fun injectManual() {
        viewModelFactory = HyperskillApp.graph().platformMainComponent.reduxViewModelFactory
        profileSettings = HyperskillApp.graph().buildProfileSettingsComponent().profileSettingsInteractor.getProfileSettings()
        analyticInteractor = HyperskillApp.graph().analyticComponent.analyticInteractor
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<AppFeature.State.Idle>()
        viewStateDelegate.addState<AppFeature.State.Loading>(viewBinding.mainProgress)
        viewStateDelegate.addState<AppFeature.State.Ready>(viewBinding.mainNavigationContainer)
        viewStateDelegate.addState<AppFeature.State.NetworkError>(viewBinding.mainError.root)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let(::handleNewIntent)
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
            is AppFeature.Action.ViewAction.NavigateTo.OnboardingScreen ->
                router.newRootScreen(OnboardingScreen)
            is AppFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                router.newRootScreen(AuthScreen)
            is AppFeature.Action.ViewAction.NavigateTo.HomeScreen ->
                router.newRootScreen(MainScreen)
            is AppFeature.Action.ViewAction.NavigateTo.NewUserScreen ->
                router.newRootScreen(PlaceholderNewUserScreen)
        }
    }

    override fun render(state: AppFeature.State) {
        viewStateDelegate.switchState(state)
        when (state) {
            is AppFeature.State.Idle, AppFeature.State.Loading ->
                window.statusBarColor = ContextCompat.getColor(this, R.color.color_black_900)
            is AppFeature.State.Ready ->
                window.statusBarColor = resolveColorAttribute(R.attr.colorPrimaryVariant)
        }
    }

    private fun handleNewIntent(intent: Intent) {
        val clickedNotificationData = with(NotificationIntentBuilder) {
            intent.getClickedNotificationData()
        }
        if (clickedNotificationData != null) {
            logNotificationClickedEvent(clickedNotificationData)
        }
    }

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
            is DefaultNotificationClickedData -> {
                // no op
            }
        }
    }
}
