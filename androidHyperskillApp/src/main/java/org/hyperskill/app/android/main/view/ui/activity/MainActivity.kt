package org.hyperskill.app.android.main.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.screen.AuthSocialScreen
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.NavigationContainer
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class MainActivity :
    AppCompatActivity(),
    NavigationContainer,
    ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction> {
    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

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

        if (savedInstanceState == null) {
            initNavigation()
        }
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .usersListComponentBuilder()
            .build()
            .inject(this)
    }

    private fun initNavigation() {
        router.newRootScreen(AuthSocialScreen)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        localCicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        localCicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {}

    override fun render(state: AuthFeature.State) {}
}
