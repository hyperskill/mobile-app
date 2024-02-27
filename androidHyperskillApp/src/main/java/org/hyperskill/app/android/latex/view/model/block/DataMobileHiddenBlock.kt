package org.hyperskill.app.android.latex.view.model.block

object DataMobileHiddenBlock : ContentBlock {
    override val header: String = """
        <script type="text/javascript" src="file:///android_asset/scripts/remove_data_mobile_hidden_elements.js"></script>
    """.trimIndent()
}