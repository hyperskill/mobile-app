package org.hyperskill.app.android.latex.view.mapper

import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import org.hyperskill.app.android.latex.view.model.LatexData
import org.hyperskill.app.android.latex.view.model.block.ContentBlock
import org.hyperskill.app.android.latex.view.model.block.HighlightScriptBlock
import org.hyperskill.app.android.latex.view.model.block.HorizontalScrollBlock
import org.hyperskill.app.android.latex.view.model.block.KotlinRunnableSamplesScriptBlock
import org.hyperskill.app.android.latex.view.model.block.LatexScriptBlock
import org.hyperskill.app.android.latex.view.model.block.MetaBlock
import org.hyperskill.app.android.latex.view.model.block.WebScriptBlock
import org.hyperskill.app.android.latex.view.model.rule.RelativePathContentRule
import org.hyperskill.app.android.latex.view.resolvers.OlLiTagHandler
import org.hyperskill.app.config.BuildKonfig

class LatexTextMapper {
    private val primaryBlocks =
        listOf(
            HighlightScriptBlock(),
            KotlinRunnableSamplesScriptBlock(),
            LatexScriptBlock(),
            WebScriptBlock()
        )

    private val regularBlocks =
        listOf(
            HorizontalScrollBlock(),
            MetaBlock(BuildKonfig.BASE_URL)
        )

    private val rules =
        listOf(
            RelativePathContentRule(BuildKonfig.BASE_URL),
        )

    private val tagHandler = OlLiTagHandler()

    fun mapToLatexText(text: String): LatexData {
        val content = rules.fold(text) { tmp, rule -> rule.process(tmp) }
        val primary = primaryBlocks.filter { it.isEnabled(content) }

        return if (primary.isEmpty()) {
            val spanned = HtmlCompat
                .fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler)
                .trimEnd(Char::isWhitespace)
                .toSpannable()

            LatexData.Text(spanned)
        } else {
            val blocks = primary + regularBlocks.filter { it.isEnabled(content) }

            LatexData.Web(
                header = blocks.joinToString(separator = "", transform = ContentBlock::header),
                preBody = blocks.joinToString(separator = "", transform = ContentBlock::preBody),
                body = content,
                postBody = blocks.asReversed().joinToString(separator = "", transform = ContentBlock::postBody),
                settings = primary.map(ContentBlock::settings).reduce { a, b -> a + b }
            )
        }
    }
}