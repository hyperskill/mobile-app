package org.hyperskill.app.android.latex.view.model.block

object RemoveIFrameElementsInjection : ContentBlock {
    override val header: String = """
        <script type="text/javascript" src="file:///android_asset/scripts/remove_iframes.js"></script>
    """.trimIndent()
}