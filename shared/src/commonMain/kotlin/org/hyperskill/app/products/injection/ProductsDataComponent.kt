package org.hyperskill.app.products.injection

import org.hyperskill.app.products.domain.interactor.ProductsInteractor

interface ProductsDataComponent {
    val productsInteractor: ProductsInteractor
}