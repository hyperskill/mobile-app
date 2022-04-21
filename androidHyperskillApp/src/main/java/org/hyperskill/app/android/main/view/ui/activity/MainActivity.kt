package org.hyperskill.app.android.main.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.screen.AuthScreen
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.presentation.MainViewModel
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.NavigationContainer
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class MainActivity :
    AppCompatActivity(),
    NavigationContainer,
    ReduxView<AppFeature.State, AppFeature.Action.ViewAction> {
    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewStateDelegate: ViewStateDelegate<AppFeature.State> = ViewStateDelegate()
    private val mainViewModelProvider: MainViewModel by reduxViewModel(this) { viewModelFactory }
    private val localCicerone: Cicerone<Router> = Cicerone.create()
    override val router: Router = localCicerone.router

    override val navigator by lazy {
        NestedAppNavigator(
            this,
            R.id.main_navigation_container,
            supportFragmentManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onBackPressedDispatcher
            .addCallback(navigator.onBackPressedCallback)

        super.onCreate(savedInstanceState)
        navigator.invalidateOnBackPressedCallback()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        injectComponent()
        setContentView(viewBinding.root)
        initViewStateDelegate()
        mainViewModelProvider.onNewMessage(AppFeature.Message.AppStarted)
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .mainComponentBuilder()
            .build()
            .inject(this)
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
