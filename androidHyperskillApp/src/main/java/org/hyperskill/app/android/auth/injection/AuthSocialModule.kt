package org.hyperskill.app.android.auth.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hyperskill.app.android.auth.presentation.AuthSocialViewModel
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.injection.AuthSocialFeatureBuilder
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object AuthSocialModule {
    @Provides
    @IntoMap
    @ViewModelKey(AuthSocialViewModel::class)
    internal fun provideAuthPresenter(authInteractor: AuthInteractor): ViewModel =
        AuthSocialViewModel(
            AuthSocialFeatureBuilder
                .build(authInteractor)
                .wrapWithViewContainer()
        )
}