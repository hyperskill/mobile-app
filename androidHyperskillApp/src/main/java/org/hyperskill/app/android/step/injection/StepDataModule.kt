package org.hyperskill.app.android.step.injection

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import org.hyperskill.app.step.data.repository.StepRepositoryImpl
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.repository.StepRepository
import org.hyperskill.app.step.remote.StepRemoteDataSourceImpl

@Module
object StepDataModule {
    @Provides
    fun provideStepRemoteDataSource(httpClient: HttpClient): StepRemoteDataSource =
        StepRemoteDataSourceImpl(httpClient)

    @Provides
    fun provideStepRepository(stepRemoteDataSource: StepRemoteDataSource): StepRepository =
        StepRepositoryImpl(stepRemoteDataSource)

    @Provides
    fun provideStepInteractor(stepRepository: StepRepository): StepInteractor =
        StepInteractor(stepRepository)
}