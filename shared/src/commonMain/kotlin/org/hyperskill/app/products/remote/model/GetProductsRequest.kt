package org.hyperskill.app.products.remote.model

class GetProductsRequest(
    val category: Category
) {
    companion object {
        private const val PARAM_CATEGORY = "category"
    }

    val parameters: Map<String, Any> =
        mapOf(
            PARAM_CATEGORY to category.parameterValue
        )

    enum class Category(val parameterValue: String) {
        STREAK("Streak"),
        COVER("Cover")
    }
}