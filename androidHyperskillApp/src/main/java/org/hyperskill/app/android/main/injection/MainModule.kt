package org.hyperskill.app.android.main.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hyperskill.app.android.main.presentation.MainViewModel
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.main.injection.AppFeatureBuilder
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object MainModule {
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal fun provideMainPresenter(authInteractor: AuthInteractor): ViewModel =
        MainViewModel(
            AppFeatureBuilder
                .build(authInteractor)
                .wrapWithViewContainer()
        )
}