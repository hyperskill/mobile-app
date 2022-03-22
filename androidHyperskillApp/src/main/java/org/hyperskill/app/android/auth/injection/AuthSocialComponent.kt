package org.hyperskill.app.android.auth.injection

import dagger.Subcomponent

@Subcomponent(
    modules = [
        AuthSocialModule::class,
        AuthSocialDataModule::class
    ]
)
interface AuthSocialComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): AuthSocialComponent
    }
}