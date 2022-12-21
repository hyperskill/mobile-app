package org.hyperskill.app.products.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.products.data.repository.ProductsRepositoryImpl
import org.hyperskill.app.products.data.source.ProductsRemoteDataSource
import org.hyperskill.app.products.domain.interactor.ProductsInteractor
import org.hyperskill.app.products.domain.repository.ProductsRepository
import org.hyperskill.app.products.remote.ProductsRemoteDataSourceImpl

class ProductsDataComponentImpl(appGraph: AppGraph) : ProductsDataComponent {
    private val productsRemoteDataSource: ProductsRemoteDataSource =
        ProductsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val productsRepository: ProductsRepository =
        ProductsRepositoryImpl(productsRemoteDataSource)

    override val productsInteractor: ProductsInteractor
        get() = ProductsInteractor(productsRepository)
}