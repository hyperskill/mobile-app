package org.hyperskill.app.android.auth.injection

import dagger.Subcomponent
import org.hyperskill.app.android.auth.view.ui.fragment.AuthSocialWebViewFragment

@Subcomponent(
    modules = [
        AuthSocialWebViewModule::class,
        AuthDataModule::class
    ]
)
interface AuthSocialWebViewComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthSocialWebViewComponent
    }

    fun inject(authSocialWebViewFragment: AuthSocialWebViewFragment)
}
