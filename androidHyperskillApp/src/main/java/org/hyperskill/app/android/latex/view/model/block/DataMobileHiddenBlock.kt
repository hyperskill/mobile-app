package org.hyperskill.app.android.latex.view.model.block

object DataMobileHiddenBlock : ContentBlock {

    private const val DATA_MOBILE_HIDDEN_TAG = "data-mobile-hidden"

    override val header: String = """
        <script type="text/javascript" src="file:///android_asset/scripts/remove_data_mobile_hidden_elements.js"></script>
    """.trimIndent()

    override fun isEnabled(content: String): Boolean =
        DATA_MOBILE_HIDDEN_TAG in content
}