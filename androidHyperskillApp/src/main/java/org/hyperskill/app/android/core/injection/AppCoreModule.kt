package org.hyperskill.app.android.core.injection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import ru.nobird.android.view.injection.base.presentation.DaggerViewModelFactory

@Module
abstract class AppCoreModule {
    @Binds
    internal abstract fun bindViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideResourceProvider(context: Context): ResourceProvider =
            ResourceProviderImpl(context)
    }
}