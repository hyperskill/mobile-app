package org.hyperskill.app.android.core.injection

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.nobird.android.view.injection.base.presentation.DaggerViewModelFactory

@Module
abstract class AppCoreModule {
    @Binds
    internal abstract fun bindViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}