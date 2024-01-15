package org.hyperskill.products.domain.model

import org.hyperskill.app.products.domain.model.Product

fun Product.Companion.stub(
    id: Long = 0L,
    price: Int = 0
): Product =
    Product(
        id = id,
        price = price
    )