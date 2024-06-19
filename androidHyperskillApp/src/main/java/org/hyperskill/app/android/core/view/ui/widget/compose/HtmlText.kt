package org.hyperskill.app.android.core.view.ui.widget.compose

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.HtmlCompat

private const val URL_TAG = "url"

@Composable
fun HtmlText(
    text: String,
    baseSpanStyle: SpanStyle? = null,
    content: @Composable (AnnotatedString) -> Unit
) {
    val annotatedString = remember(text) {
        HtmlCompat
            .fromHtml(
                text.replace("\n", "<br>"),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            .toAnnotatedString(
                baseSpanStyle = baseSpanStyle,
                underlineLinks = false
            )
    }
    content(annotatedString)
}

@Composable
fun ClickableHtmlText(
    text: String,
    modifier: Modifier = Modifier,
    baseSpanStyle: SpanStyle? = null,
    linkColor: Color = Color.Blue,
    isHighlightLink: Boolean = false,
    style: TextStyle = LocalTextStyle.current,
    onUrlClick: ((url: String) -> Unit)? = null
) {
    val annotatedString = remember(text) {
        HtmlCompat
            .fromHtml(
                text.replace("\n", "<br>"),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
            .toAnnotatedString(
                baseSpanStyle = baseSpanStyle,
                linkColor = if (isHighlightLink) linkColor else Color.Unspecified,
                underlineLinks = false
            )
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = style,
    ) { offset ->
        annotatedString.getStringAnnotations(URL_TAG, offset, offset).firstOrNull()?.let { range ->
            if (onUrlClick != null) {
                onUrlClick(range.item)
            } else {
                uriHandler.openUri(range.item)
            }
        }
    }
}

fun Spanned.toAnnotatedString(
    baseSpanStyle: SpanStyle?,
    underlineLinks: Boolean,
    linkColor: Color = Color.Blue
): AnnotatedString =
    buildAnnotatedString {
        val spanned = this@toAnnotatedString
        append(spanned.toString())
        baseSpanStyle?.let { addStyle(it, 0, length) }
        getSpans(0, spanned.length, Any::class.java).forEach { span ->
            val start = getSpanStart(span)
            val end = getSpanEnd(span)
            when (span) {
                is StyleSpan -> when (span.style) {
                    Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                    Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                    Typeface.BOLD_ITALIC -> addStyle(
                        SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                        start,
                        end
                    )
                }
                is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                is ForegroundColorSpan -> addStyle(SpanStyle(color = Color(span.foregroundColor)), start, end)
                is URLSpan -> {
                    addStyle(
                        SpanStyle(
                            textDecoration = if (underlineLinks) TextDecoration.Underline else null,
                            color = linkColor
                        ),
                        start,
                        end
                    )
                    addStringAnnotation(URL_TAG, span.url, start, end)
                }
            }
        }
    }

@Preview
@Composable
private fun LinksHtmlTextPreview() {
    ClickableHtmlText(
        /*ktlint-disable*/
        text = "<b>Some text</b> \n<a href=\"https://developer.android.com/jetpack/androidx/releases/compose\" target=\"_blank\">" +
            "link text</a>, the rest of the text"
    )
}