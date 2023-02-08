package org.hyperskill.app.android.latex.view.model.block

class HighlightScriptBlock : ContentBlock {
    override val header: String = """
        <script type="text/javascript" src="file:///android_asset/scripts/highlight.pack.js"></script>
        <script>hljs.initHighlightingOnLoad();</script>
        <script type="text/javascript" src="file:///android_asset/scripts/lines_wrapper.js"></script>
    """.trimIndent()

    override fun isEnabled(content: String): Boolean =
        "<code" in content
}