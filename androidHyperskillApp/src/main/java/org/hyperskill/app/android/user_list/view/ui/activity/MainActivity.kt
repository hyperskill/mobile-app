package org.hyperskill.app.android.user_list.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.android.user_list.presentation.UserListViewModel
import org.hyperskill.app.android.user_list.view.ui.adapter.UsersAdapter
import org.hyperskill.app.user_list.remote.model.UsersQuery
import org.hyperskill.app.user_list.presentation.UsersListFeature
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ReduxView<UsersListFeature.State, UsersListFeature.Action.ViewAction> {
    private lateinit var viewBinding: ActivityMainBinding
    private val usersAdapter = UsersAdapter()

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val userListViewModel: UserListViewModel by reduxViewModel(this) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        injectComponent()

        viewBinding.button.setOnClickListener {
            userListViewModel.onNewMessage(
                UsersListFeature.Message.Init(
                    forceUpdate = true,
                    usersQuery = UsersQuery(
                        userName = viewBinding.userName.text.toString()
                    )
                )
            )
        }
        viewBinding.usersList.adapter = usersAdapter
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .usersListComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onAction(action: UsersListFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: UsersListFeature.State) {
        usersAdapter.updateList((state as? UsersListFeature.State.Data)?.users)
    }
}
