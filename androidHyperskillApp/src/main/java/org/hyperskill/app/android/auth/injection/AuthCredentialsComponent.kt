package org.hyperskill.app.android.auth.injection

import dagger.Subcomponent
import org.hyperskill.app.android.auth.view.ui.fragment.AuthCredentialsFragment

@Subcomponent(
    modules = [
        AuthCredentialsModule::class,
        AuthDataModule::class
    ]
)
interface AuthCredentialsComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthCredentialsComponent
    }

    fun inject(authCredentialsFragment: AuthCredentialsFragment)
}
