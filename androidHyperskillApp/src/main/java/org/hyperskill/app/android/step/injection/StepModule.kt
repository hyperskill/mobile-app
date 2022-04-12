package org.hyperskill.app.android.step.injection

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.hyperskill.app.android.step.presentation.StepViewModel
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.injection.StepFeatureBuilder
import ru.nobird.android.view.injection.base.presentation.ViewModelKey
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

@Module
object StepModule {
    @Provides
    @IntoMap
    @ViewModelKey(StepViewModel::class)
    internal fun provideStepPresenter(stepInteractor: StepInteractor): ViewModel =
        StepViewModel(
            StepFeatureBuilder
                .build(stepInteractor)
                .wrapWithViewContainer()
        )
}