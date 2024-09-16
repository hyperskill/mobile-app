package org.hyperskill.app.code.injection

import org.hyperskill.app.code.data.repository.CodeRepositoryImpl
import org.hyperskill.app.code.data.source.CodeRemoteDataSource
import org.hyperskill.app.code.domain.repository.CodeRepository
import org.hyperskill.app.code.remote.CodeRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

internal class CodeDataComponentImpl(appGraph: AppGraph) : CodeDataComponent {

    private val codeRemoteDataSource: CodeRemoteDataSource =
        CodeRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val codeRepository: CodeRepository = CodeRepositoryImpl(codeRemoteDataSource)
}