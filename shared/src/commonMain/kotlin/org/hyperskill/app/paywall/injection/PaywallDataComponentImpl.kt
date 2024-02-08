package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.paywall.cache.PaywallCacheDataSourceImpl
import org.hyperskill.app.paywall.data.repository.PaywallRepositoryImpl
import org.hyperskill.app.paywall.domain.model.PaywallRepository

internal class PaywallDataComponentImpl(
    appGraph: AppGraph
) : PaywallDataComponent {

    private val paywallCacheDataSource =
        PaywallCacheDataSourceImpl(appGraph.commonComponent.settings)

    override val paywallRepository: PaywallRepository =
        PaywallRepositoryImpl(paywallCacheDataSource)
}