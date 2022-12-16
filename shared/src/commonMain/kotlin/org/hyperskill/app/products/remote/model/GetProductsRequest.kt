package org.hyperskill.app.products.remote.model

import ru.nobird.app.core.model.mapOfNotNull

class GetProductsRequest(
    val category: Category
) {
    companion object {
        private const val PARAM_CATEGORY = "category"
    }

    val parameters: Map<String, Any> =
        mapOfNotNull(
            PARAM_CATEGORY to category.parameterValue
        )

    enum class Category(val parameterValue: String) {
        STREAK("Streak"),
        COVER("Cover")
    }
}