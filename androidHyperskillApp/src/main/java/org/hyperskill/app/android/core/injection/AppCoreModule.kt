package org.hyperskill.app.android.core.injection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import org.hyperskill.app.auth.injection.AuthDataBuilder
import ru.nobird.android.view.injection.base.presentation.DaggerViewModelFactory
import javax.inject.Singleton

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

        @Provides
        @JvmStatic
        @Singleton
        fun provideAuthorizationFlow(): Flow<UserDeauthorized> =
            AuthDataBuilder.provideAuthorizationFlow()

        @Provides
        @JvmStatic
        @Singleton
        fun provideAuthCacheMutex(): Mutex =
            AuthDataBuilder.provideAuthorizationCacheMutex()
    }
}