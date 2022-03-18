package org.hyperskill.app.android.user_list.injection

import dagger.Subcomponent
import org.hyperskill.app.android.user_list.view.ui.activity.MainActivity

@Subcomponent(
    modules = [
        UserListModule::class,
        UserListDataModule::class
    ]
)
interface UserListComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): UserListComponent
    }

    fun inject(mainActivity: MainActivity)
}