package org.hyperskill.app.purchases.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.purchases.domain.model.PurchaseManager

class PurchaseComponentImpl(
    purchaseManager: PurchaseManager,
    analyticInteractor: AnalyticInteractor
) : PurchaseComponent {

    override val purchaseInteractor: PurchaseInteractor =
        PurchaseInteractor(purchaseManager, analyticInteractor)
}