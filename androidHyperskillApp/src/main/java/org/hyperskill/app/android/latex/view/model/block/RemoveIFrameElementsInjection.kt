package org.hyperskill.app.android.latex.view.model.block

object RemoveIFrameElementsInjection : ContentBlock {

    private const val I_FRAME_TAG = "iframe"

    override val header: String = """
        <script type="text/javascript" src="file:///android_asset/scripts/remove_iframes.js"></script>
    """.trimIndent()

    override fun isEnabled(content: String): Boolean =
        I_FRAME_TAG in content
}