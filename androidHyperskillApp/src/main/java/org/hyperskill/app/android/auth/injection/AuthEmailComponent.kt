package org.hyperskill.app.android.auth.injection

import dagger.Subcomponent
import org.hyperskill.app.android.auth.view.ui.fragment.AuthEmailFragment

@Subcomponent(
    modules = [
        AuthEmailModule::class,
        AuthEmailDataModule::class
    ]
)
interface AuthEmailComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthEmailComponent
    }

    fun inject(authEmailFragment: AuthEmailFragment)
}
