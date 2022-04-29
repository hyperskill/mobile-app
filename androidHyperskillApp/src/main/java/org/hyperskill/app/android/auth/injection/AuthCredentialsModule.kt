package org.hyperskill.app.android.auth.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hyperskill.app.android.auth.presentation.AuthCredentialsViewModel
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.injection.AuthCredentialsFeatureBuilder
import org.hyperskill.app.auth.view.mapper.AuthCredentialsErrorMapper
import org.hyperskill.app.core.view.mapper.ResourceProvider
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object AuthCredentialsModule {
    @Provides
    @IntoMap
    @ViewModelKey(AuthCredentialsViewModel::class)
    internal fun provideAuthPresenter(authInteractor: AuthInteractor): ViewModel =
        AuthCredentialsViewModel(
            AuthCredentialsFeatureBuilder
                .build(authInteractor)
                .wrapWithViewContainer()
        )

    @Provides
    @JvmStatic
    internal fun provideAuthCredentialsErrorMapper(resourceProvider: ResourceProvider): AuthCredentialsErrorMapper =
        AuthCredentialsErrorMapper(resourceProvider)
}
