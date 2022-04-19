package org.hyperskill.app.android.core.injection

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.auth.injection.AuthEmailComponent
import org.hyperskill.app.android.auth.injection.AuthSocialComponent
import org.hyperskill.app.android.network.injection.AndroidNetworkModule
import org.hyperskill.app.android.step.injection.StepComponent
import org.hyperskill.app.android.user_list.injection.UserListComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppCoreModule::class,
        SharedPreferencesModule::class,
        AndroidNetworkModule::class
    ]
)
interface AppCoreComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppCoreComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

    fun inject(hyperskillApp: HyperskillApp)

    fun usersListComponentBuilder(): UserListComponent.Builder
    fun authSocialComponentBuilder(): AuthSocialComponent.Builder
    fun authEmailComponentBuilder(): AuthEmailComponent.Builder
    fun stepComponentBuilder(): StepComponent.Builder
}