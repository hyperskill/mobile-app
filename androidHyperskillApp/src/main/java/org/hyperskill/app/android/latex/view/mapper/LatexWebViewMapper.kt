package org.hyperskill.app.android.latex.view.mapper

import android.content.Context
import org.hyperskill.app.android.latex.view.model.LatexData
import org.hyperskill.app.android.latex.view.model.TextAttributes
import org.hyperskill.app.android.latex.view.model.block.BaseStyleBlock
import org.hyperskill.app.android.latex.view.model.block.ContentBlock
import org.hyperskill.app.android.latex.view.model.block.SelectionColorStyleBlock

class LatexWebViewMapper(private val context: Context) {
    companion object {
        private const val ASSETS = "file:///android_asset/"
        private const val FONTS = "fonts/"
        private const val EXTENSION = ".ttf"
    }

    fun mapLatexData(webData: LatexData.Web, attributes: TextAttributes): String {
        val fontPath = "$ASSETS$FONTS${context.resources.getResourceEntryName(attributes.fontResId)}$EXTENSION"

        val blocks =
            listOf(
                BaseStyleBlock(fontPath, attributes.textColor),
                SelectionColorStyleBlock(attributes.textColorHighlight)
            )

        val header = webData.header + blocks.joinToString(separator = "", transform = ContentBlock::header)
        val preBody = webData.preBody + blocks.joinToString(separator = "", transform = ContentBlock::preBody)
        val postBody = blocks.asReversed().joinToString(
            separator = "",
            transform = ContentBlock::postBody
        ) + webData.postBody // order is reversed to preserve tags

        return """
            <!DOCTYPE html>
            <html>
            <head>
                $header
            </head>
            <body style='margin:0;padding:0;'>
                $preBody
                ${webData.body}
                $postBody
            </body>
            </html>
        """.trimIndent()
    }
}