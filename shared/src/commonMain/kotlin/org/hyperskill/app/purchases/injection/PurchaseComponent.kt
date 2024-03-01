package org.hyperskill.app.purchases.injection

import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor

interface PurchaseComponent {
    val purchaseInteractor: PurchaseInteractor
}