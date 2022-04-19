package org.hyperskill.app.android.main.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.android.main.presentation.MainViewModel
import org.hyperskill.app.main.injection.AppFeatureBuilder
import org.hyperskill.app.main.presentation.AppFeature
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object MainModule {
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal fun provideMainPresenter(authorizationFlow: MutableSharedFlow<AppFeature.Message>): ViewModel =
        MainViewModel(
            AppFeatureBuilder
                .build(authorizationFlow)
                .wrapWithViewContainer()
        )
}