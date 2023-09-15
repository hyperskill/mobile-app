package org.hyperskill.app.android.code.view

import android.text.Spannable
import kotlin.math.min
import org.hyperskill.app.android.code.presentation.highlight.syntaxhighlight.PrettifyParseResult
import org.hyperskill.app.android.code.view.model.themes.CodeTheme

/**
 * Convert [PrettifyParseResult] to the [CodeSyntaxSpan] and apply to the [Spannable]
 */
fun Spannable.applyPrettifyParseResults(
    prettifyParseResults: List<PrettifyParseResult>,
    start: Int,
    end: Int,
    theme: CodeTheme
): Spannable {
    filterParseResult(start, end, prettifyParseResults, theme)
        .forEach { parseResult ->
            val codeSyntaxSpan = mapParseResultToSpan(parseResult, theme)
            if (codeSyntaxSpan != null) {
                this.setSpan(
                    /* what = */ codeSyntaxSpan,
                    /* start = */ parseResult.offset,
                    /* end = */ min(parseResult.offset + parseResult.length, this.length),
                    /* flags = */ Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    return this
}

private fun filterParseResult(
    start: Int,
    end: Int,
    prettifyParseResults: List<PrettifyParseResult>,
    theme: CodeTheme,
): Sequence<PrettifyParseResult> =
    prettifyParseResults
        .asSequence()
        .filterNot { it.offset + it.length < start || it.offset > end }
        .filter { theme.syntax.shouldBePainted(it.styleKeysString) }

private fun mapParseResultToSpan(parseResult: PrettifyParseResult, theme: CodeTheme): CodeSyntaxSpan? =
    theme.syntax.colorMap[parseResult.styleKeysString]?.let { color ->
        CodeSyntaxSpan(color)
    }