package org.hyperskill.app.android.auth.injection

import dagger.Subcomponent
import org.hyperskill.app.android.auth.view.ui.fragment.AuthSocialFragment

@Subcomponent(
    modules = [
        AuthSocialModule::class,
        AuthDataModule::class
    ]
)
interface AuthSocialComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthSocialComponent
    }

    fun inject(authSocialFragment: AuthSocialFragment)
}