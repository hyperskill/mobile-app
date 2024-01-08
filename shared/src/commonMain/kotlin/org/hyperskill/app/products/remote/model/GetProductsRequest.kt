package org.hyperskill.app.products.remote.model

class GetProductsRequest(
    val type: ProductType
) {
    companion object {
        private const val PARAM_TYPE = "type"
    }

    val parameters: Map<String, Any> =
        mapOf(
            PARAM_TYPE to type.parameterValue
        )

    enum class ProductType(val parameterValue: String) {
        STREAK_FREEZE("streak freeze")
    }
}