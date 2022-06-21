package org.hyperskill.app.android.latex.view.model.block

import androidx.annotation.ColorInt

class SelectionColorStyleBlock(
    @ColorInt
    selectionColor: Int
) : ContentBlock {
    override val header: String = """
        <style>
            ::selection { background: #${Integer.toHexString(selectionColor).substring(2)}; }
        </style>
    """.trimIndent()
}