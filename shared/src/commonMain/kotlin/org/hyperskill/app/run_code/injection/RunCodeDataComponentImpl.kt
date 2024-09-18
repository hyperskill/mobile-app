package org.hyperskill.app.run_code.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.run_code.data.repository.RunCodeRepositoryImpl
import org.hyperskill.app.run_code.data.source.RunCodeRemoteDataSource
import org.hyperskill.app.run_code.domain.repository.RunCodeRepository
import org.hyperskill.app.run_code.remote.RunCodeRemoteDataSourceImpl

internal class RunCodeDataComponentImpl(appGraph: AppGraph) : RunCodeDataComponent {
    private val runCodeRemoteDataSource: RunCodeRemoteDataSource =
        RunCodeRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val runCodeRepository: RunCodeRepository =
        RunCodeRepositoryImpl(runCodeRemoteDataSource)
}