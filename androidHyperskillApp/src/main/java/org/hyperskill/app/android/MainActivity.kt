package org.hyperskill.app.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hyperskill.app.android.databinding.ActivityMainBinding
import org.hyperskill.app.user_list.remote.model.UsersQuery
import org.hyperskill.app.user_list.presentation.UsersListFeature
import org.hyperskill.app.user_list.injection.UsersListFeatureBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val usersAdapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val usersListFeature = UsersListFeatureBuilder.build()

        usersListFeature.addStateListener(this::setState)

        viewBinding.button.setOnClickListener {
            usersListFeature.onNewMessage(
                UsersListFeature.Message.Init(
                    forceUpdate = true,
                    usersQuery = UsersQuery(
                        userName = viewBinding.userName.text.toString()
                    )
                )
            )
        }

        setState(usersListFeature.state)
        viewBinding.usersList.adapter = usersAdapter
    }

    private fun setState(state: UsersListFeature.State) {
        // TODO: 7/21/21 change to paged list
        usersAdapter.updateList((state as? UsersListFeature.State.Data)?.users)
    }
}
