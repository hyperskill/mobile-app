package org.hyperskill.app.android.user_list.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.user_list.view.ui.adapter.UsersAdapter
import org.hyperskill.app.auth.presentation.AuthFeature
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject


class MainActivity : AppCompatActivity(), ReduxView<AuthFeature.State, AuthFeature.Action.ViewAction> {
    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        injectComponent()
        setContentView(viewBinding.root)
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .usersListComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onAction(action: AuthFeature.Action.ViewAction) {}

    override fun render(state: AuthFeature.State) {}
}
