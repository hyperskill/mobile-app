package org.hyperskill.app.android.auth.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hyperskill.app.android.auth.presentation.AuthSocialWebViewViewModel
import org.hyperskill.app.auth.injection.AuthSocialWebViewFeatureBuilder
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object AuthSocialWebViewModule {
    @Provides
    @IntoMap
    @ViewModelKey(AuthSocialWebViewViewModel::class)
    internal fun provideAuthPresenter(): ViewModel =
        AuthSocialWebViewViewModel(
            AuthSocialWebViewFeatureBuilder
                .build()
                .wrapWithViewContainer()
        )
}
