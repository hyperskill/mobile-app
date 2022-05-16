package org.hyperskill.app

import kotlinx.serialization.SerialName

interface Block

interface InlineBlock

interface WithJsonName {
    val name: String
}

interface WithJsonNames {
    val names: Set<String>
}

/* Example JSON:
{
  "style": {
    "color": "#892621",
    "background-color": "#45776D",
    "font-size": 17,
    "text-align": "left"
  }
}
*/
data class InlineStyle(
    override val name: String = "style",
    @SerialName("color")
    val color: String?,
    @SerialName("background-color")
    val backgroundColor: String?,
    @SerialName("font-size")
    val fontSize: Int?,
    @SerialName("text-align")
    val textAlignment: String?
) : WithJsonName

data class TextBlock(
    override val name: String = "p",
    @SerialName("style")
    val style: InlineStyle?
    // plain text or blocks:
    // code, strong, span, em, img, a, br, b, samp, u, div, p, sup, i, sub, tt, iframe (2), strike (1), pre (1), li (1), ul (1), h5 (1)
) : Block, WithJsonName

/* Example JSON:
{
  "strong": {
    "text": "This text is bold!",
    "style": {
      "font-size": 17,
      "color": "#892621"
    }
  }
}
*/
data class BoldTextInlineBlock(
    override val names: Set<String> = setOf("strong", "b"),
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonNames

/* Example JSON:
{
  "em": {
    "text": "This is emphasized text.",
    "style": {
      "font-size": 17,
      "color": "#892621"
    }
  }
}
*/
data class ItalicTextInlineBlock(
    override val names: Set<String> = setOf("em", "i"),
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonNames

/* Example JSON:
{
  "samp": {
    "text": "The text is displayed in the monospace font."
  }
}
*/
data class SampleOutputTextInlineBlock(
    override val name: String = "samp",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "u": {
    "text": "The text is displayed with an underline.",
    "style": {
      "font-size": 17,
      "color": "#892621"
    }
  }
}
*/
data class UnderlineTextInlineBlock(
    override val name: String = "u",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "sup": {
    "text": "Superscript text appears half a character above the normal line, and rendered in a smaller font."
  }
}
*/
data class SuperscriptTextInlineBlock(
    override val name: String = "sup",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "sub": {
    "text": "Subscript text appears half a character below the normal line, and rendered in a smaller font."
  }
}
*/
data class SubscriptTextInlineBlock(
    override val name: String = "sub",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "tt": {
    "text": "This text is monospace text."
  }
}
*/
data class MonospaceTextInlineBlock(
    override val name: String = "tt",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "strike": {
    "text": "This text is strikethrough text."
  }
}
*/
data class StrikethroughTextInlineBlock(
    override val names: Set<String> = setOf("s", "strike"),
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonNames

/* Example JSON:
{
  "br": {}
}
*/
data class LineBreak(
    override val name: String = "br"
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "code": {
    "language": "python3",
    "code": "print(\"Hello World\")"
  }
}
*/
data class CodeBlock(
    override val name: String = "code",
    @SerialName("language")
    val language: String,
    @SerialName("code")
    val code: String
) : Block, WithJsonName

/* Example JSON:
{
  "math-tex": {
    "latex": "x^2 + y^2 = z^2"
  }
}
*/
data class MathFormula(
    override val name: String = "math-tex",
    @SerialName("latex")
    val latex: String
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "h5": {
    "text": "This is heading 5",
    "style": {
      "text-align": "center",
      "background-color": "#45776d",
      "color": "#892621"
    }
  }
}
*/
data class Heading(
    override val names: Set<String> = setOf("h1", "h2", "h3", "h4", "h5", "h6"),
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : Block, WithJsonNames

/* Example JSON:
{
  "a": {
    "href": "https://hyperskill.org",
    "text": "Visit hyperskill.org!",
    "style": {
      "color": "#892621"
    }
  }
}
*/
data class Hyperlink(
    override val name: String = "a",
    @SerialName("href")
    val url: String,
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: InlineStyle?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "img": {
    "src": "https://i.picsum.photos/id/211/200/300.jpg?hmac=wrwgBoS1KPKiLCrxowMtMQ7NpVlzI1NvocRSpH6HSm0",
    "alt": "An alternate text for the image"
  }
}
*/
data class ImageBlock(
    override val name: String = "img",
    @SerialName("src")
    val srcUrl: String,
    @SerialName("alt")
    val alternateText: String,
    @SerialName("style")
    val style: InlineStyle?
) : Block, WithJsonName

data class ListBlock(
    override val names: Set<String> = setOf("ul", "ol"),
    @SerialName("li")
    val items: List<ListItem>
) : Block, WithJsonNames

data class ListItem(
    override val name: String = "li"
// plain text or
// content: code, p, pre, strong, span, em, br, a, img, li, ul, ol, samp, u, sup, div, i, b, strike
) : Block, WithJsonName

data class PreBlock(
    override val name: String = "pre"
// content: code, pre, br, span, strong, samp
) : Block, WithJsonName

data class SpanInlineBlock(
    override val name: String = "span"
// content: span, code, strong, img, em, div, p, samp, br, b, a, iframe, pre, tt, ul, u, li, h5,
) : InlineBlock, WithJsonName

/*
<table>
  <caption>Monthly savings</caption>
  <tr>
    <th>Month</th>
    <th>Savings</th>
  </tr>
  <tr>
    <td>January</td>
    <td style="text-align:right">$100</td>
  </tr>
  <tr>
    <td>February</td>
    <td style="text-align:right">$80</td>
  </tr>
</table>

to JSON

{
  "table": {
    "caption": {
      "text": "Monthly savings"
    },
    "rows": [
      {
        "type": "th",
        "cells": [
          {
            "text": "Month"
          },
          {
            "text": "Savings"
          }
        ]
      },
      {
        "type": "td",
        "cells": [
          {
            "text": "January"
          },
          {
            "text": "$100",
            "style": {
              "text-align": "right"
            }
          }
        ]
      },
      {
        "type": "td",
        "cells": [
          {
            "text": "February"
          },
          {
            "text": "$80",
            "style": {
              "text-align": "right"
            }
          }
        ]
      }
    ]
  }
}
*/
data class TableBlock(
    override val name: String = "table",
    @SerialName("caption")
    val caption: TableCaption,
    @SerialName("rows")
    val rows: List<TableRow>
) : Block, WithJsonName

data class TableCaption(
    override val name: String = "caption",
    @SerialName("text")
    val text: String
    // plain text or p block
) : Block, WithJsonName

data class TableRow(
    override val name: String = "tr",
    @SerialName("type")
    val type: String, // th or td
//    @SerialName("cells")
//    val cells: List<TableDataCell || TableHeaderCell>
) : Block, WithJsonName

data class TableDataCell(
    override val name: String = "td",
    @SerialName("style")
    val style: InlineStyle?
// content: plain text or
// strong, span, p, code, em, samp, img, pre, sup, br, li, ul, a, strike, u
) : Block, WithJsonName

data class TableHeaderCell(
    override val name: String = "th",
    @SerialName("style")
    val style: InlineStyle?
// content: plain text or
// span, strong, code, u, p, em
) : Block, WithJsonName

/* Example JSON:
{
  "figure": {
    "img": {
      "src": "pic_trulli.jpg",
      "alt": "Trulli"
    },
    "figcaption": {
      "text": "Fig.1 - Trulli, Puglia, Italy."
    }
  }
}
*/
data class FigureBlock(
    override val name: String = "figure",
    @SerialName("img")
    val image: ImageBlock,
    @SerialName("figcaption")
    val caption: FigureCaptionBlock
) : Block, WithJsonName

/* Example JSON:
{
  "figcaption": {
    "text": "Defines a caption for the figure."
  }
}
*/
data class FigureCaptionBlock(
    override val name: String = "figcaption",
    @SerialName("text")
    val text: String
    // plain text or strong, a
) : Block, WithJsonName

/* Example JSON:
{
  "blockquote": {
    "text": "Defines a blockquote."
  }
}
*/
data class BlockquoteBlock(
    override val name: String = "blockquote",
    @SerialName("text")
    val text: String
    // plain text or p, em, br, samp, li, a, ul
) : Block, WithJsonName

data class DetailsBlock(
    override val name: String = "details",
    @SerialName("summary")
    val summary: String,
    val blocks: List<Block> // code, pre, br, span, p, img, ul, li,
) : Block, WithJsonName

// APPSUX-95
/* Example JSON:
{
  "iframe": {
    "src": "https://hyperskill.org/"
  }
}
*/
data class IframeBlock(
    override val name: String = "iframe",
    @SerialName("src")
    val url: String?
) : Block, WithJsonName

/* Example JSON:
{
  "kotlin-runnable": {
    "html": "<kotlin-runnable theme=\"darcula\">\n<pre><code class=\"language-kotlin\">\nfun main(args : Array &lt;String&gt; ) {\n//sampleStart\n    println(\"Hello Kotliner!\")\n//sampleEnd\n}\n</code></pre>\n</kotlin-runnable>"
  }
}
*/
data class KotlinRunnableBlock(
    override val name: String = "kotlin-runnable",
    @SerialName("html")
    val htmlText: String
) : Block, WithJsonName