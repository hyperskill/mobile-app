package org.hyperskill.app.purchases.injection

import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.purchases.domain.model.PurchaseManager

class PurchaseComponentImpl(
    purchaseManager: PurchaseManager
) : PurchaseComponent {

    override val purchaseInteractor: PurchaseInteractor =
        PurchaseInteractor(purchaseManager)
}