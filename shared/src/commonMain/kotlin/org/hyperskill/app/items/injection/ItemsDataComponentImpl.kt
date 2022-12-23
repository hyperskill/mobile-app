package org.hyperskill.app.items.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.items.data.repository.ItemsRepositoryImpl
import org.hyperskill.app.items.data.source.ItemsRemoteDataSource
import org.hyperskill.app.items.domain.interactor.ItemsInteractor
import org.hyperskill.app.items.domain.repository.ItemsRepository
import org.hyperskill.app.items.remote.ItemsRemoteDataSourceImpl

class ItemsDataComponentImpl(appGraph: AppGraph) : ItemsDataComponent {
    private val itemsRemoteDataSource: ItemsRemoteDataSource =
        ItemsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val itemsRepository: ItemsRepository =
        ItemsRepositoryImpl(itemsRemoteDataSource)

    override val itemsInteractor: ItemsInteractor
        get() = ItemsInteractor(itemsRepository)
}