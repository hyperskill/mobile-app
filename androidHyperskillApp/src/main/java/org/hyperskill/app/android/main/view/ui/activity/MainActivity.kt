package org.hyperskill.app.android.main.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.fragment.AuthFragment
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.AppNavigationContainer
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
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

    override val navigator by lazy {
        NestedAppNavigator(
            this,
            R.id.mainNavigationContainer,
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onBackPressedDispatcher
            .addCallback(navigator.onBackPressedCallback)

        super.onCreate(savedInstanceState)
        injectManual()
        navigator.invalidateOnBackPressedCallback()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViewStateDelegate()
        mainViewModelProvider.onNewMessage(AppFeature.Message.AppStarted)

        lifecycleScope.launch {
            router
                .observeResult(AuthFragment.AUTH_SUCCESS)
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    mainViewModelProvider.onNewMessage(AppFeature.Message.UserAuthorized)
                }
        }
    }

    private fun injectManual() {
        viewModelFactory = HyperskillApp.graph().platformMainComponent.manualViewModelFactory
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<AppFeature.State.Idle>(viewBinding.mainSplash.root)
        viewStateDelegate.addState<AppFeature.State.Loading>(viewBinding.mainSplash.root)
        viewStateDelegate.addState<AppFeature.State.Ready>(viewBinding.mainNavigationContainer)
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
            is AppFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                router.newRootScreen(AuthScreen)
            is AppFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                router.newRootScreen(HomeScreen)
            }
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
}
