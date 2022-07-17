package org.hyperskill.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    "font-weight": 14,
    "text-align": "left"
  }
}
*/
data class Style(
    override val name: String = "style",
    @SerialName("color")
    val color: String?,
    @SerialName("background-color")
    val backgroundColor: String?,
    @SerialName("font-size")
    val fontSize: Int?,
    @SerialName("font-weight")
    val fontWeight: Int?,
    @SerialName("text-align")
    val textAlignment: TextAlignment?
) : WithJsonName {
    @Serializable
    enum class TextAlignment {
        @SerialName("left")
        LEFT,
        @SerialName("right")
        RIGHT,
        @SerialName("center")
        CENTER,
        @SerialName("justify")
        JUSTIFY
    }
}

/* Example JSON:
{
  "p": {
    "text": "This is some text in a paragraph.",
    "style": {
      "font-size": 17
    }
  }
}

{
  "p": {
    "tokens": [
      {
        "math-tex": {
          "latex": "x^2 + y^2 = z^2"
        }
      },
      {
        "br": {}
      },
      {
        "strong": {
          "text": "This text is bold!",
          "style": {
            "color": "#892621"
          }
        }
      }
    ],
    "style": {
      "font-size": 17
    }
  }
}
*/
data class TextBlock(
    override val name: String = "p",
    val value: Value,
    @SerialName("style")
    val style: Style?
) : Block, WithJsonName {
    sealed class Value {
        data class PlainText(
            @SerialName("text")
            val text: String
        ) : Value()

        data class RichText(
            @SerialName("tokens")
            val inlineTokens: List<InlineToken>
        ) : Value() {
            sealed class InlineToken {
                data class MathFormula(val mathFormula: MathFormulaInlineBlock) : InlineToken()

                data class Span(val span: SpanInlineBlock) : InlineToken()

                data class LineBreak(val lineBreak: LineBreakInlineBlock) : InlineToken()

                data class Hyperlink(val hyperlink: HyperlinkInlineBlock) : InlineToken()

                data class BoldText(val boldText: BoldTextInlineBlock) : InlineToken()

                data class ItalicText(val italicText: ItalicTextInlineBlock) : InlineToken()

                data class UnderlineText(
                    val underlineText: UnderlineTextInlineBlock
                ) : InlineToken()

                data class SuperscriptText(
                    val superscriptText: SuperscriptTextInlineBlock
                ) : InlineToken()

                data class SubscriptText(
                    val subscriptText: SubscriptTextInlineBlock
                ) : InlineToken()

                data class MonospaceText(
                    val monospaceText: MonospaceTextInlineBlock
                ) : InlineToken()

                data class SampleOutputText(
                    val sampleOutputText: SampleOutputTextInlineBlock
                ) : InlineToken()

                data class StrikethroughText(
                    val strikethroughText: StrikethroughTextInlineBlock
                ) : InlineToken()
            }
        }
    }
}

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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
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
    val style: Style?
) : InlineBlock, WithJsonNames

/* Example JSON:
{
  "br": {}
}
*/
data class LineBreakInlineBlock(
    override val name: String = "br"
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "math-tex": {
    "latex": "x^2 + y^2 = z^2"
  }
}
*/
data class MathFormulaInlineBlock(
    override val name: String = "math-tex",
    @SerialName("latex")
    val latex: String
) : InlineBlock, WithJsonName

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
data class HyperlinkInlineBlock(
    override val name: String = "a",
    @SerialName("href")
    val url: String,
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: Style?
) : InlineBlock, WithJsonName

/* Example JSON:
{
  "span": {
    "text": "blue",
    "style": {
      "color": "#0000FF"
    }
  }
}
*/
data class SpanInlineBlock(
    override val name: String = "span",
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: Style?
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
data class HeadingBlock(
    override val names: Set<String> = setOf("h1", "h2", "h3", "h4", "h5", "h6"),
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: Style?
) : Block, WithJsonNames

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
    val style: Style?
) : Block, WithJsonName

/* Example JSON:
<ol>
  <li>Coffee</li>
  <li>Tea
    <ul>
      <li>Black tea</li>
      <li>Green tea</li>
    </ul>
  </li>
  <li>Milk</li>
</ol>

{
  "ol": {
    "items": [
      { "p": { "text": "Coffee" } },
      {
        "p": { "text": "Tea" },
        "sublist": {
          "ul": {
            "items": [
              { "p": { "text": "Black tea" } },
              { "p": { "text": "Green tea" } }
            ]
          }
        }
      },
      { "p": { "text": "Milk" } }
    ]
  }
}
*/
data class ListBlock(
    override val names: Set<String> = setOf("ul", "ol"),
    @SerialName("items")
    val items: List<ListItemBlock>
) : Block, WithJsonNames

/* Example JSON:
{
  "li": {
    "p": {
      "text": "This is some text in a paragraph."
    }
  }
}

{
  "li": {
    "code": {
      "language": "python3",
      "code": "print(\"Hello World\")"
    }
  }
}
*/
data class ListItemBlock(
    override val name: String = "li",
    val value: Value,
    @SerialName("sublist")
    val sublist: ListBlock?
) : Block, WithJsonName {
    sealed class Value {
        data class Text(val text: TextBlock) : Value()

        data class Code(val code: CodeBlock) : Value()

        data class Image(val image: ImageBlock) : Value()

        data class PreformattedText(val preformattedText: PreformattedTextBlock) : Value()
    }
}

/* Example JSON:
{
  "pre": {
    "p": {
      "text": "This text preserves        spaces"
    }
  }
}
*/
data class PreformattedTextBlock(
    override val name: String = "pre",
    val value: Value
) : Block, WithJsonName {
    sealed class Value {
        data class Text(val text: TextBlock) : Value()

        data class Code(val code: CodeBlock) : Value()
    }
}

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
      "p": { "text": "Monthly savings" }
    },
    "rows": [
      {
        "type": "th",
        "cells": [
          { "p": { "text": "Month" } },
          { "p": { "text": "Savings" } }
        ]
      },
      {
        "type": "td",
        "cells": [
          { "p": { "text": "January" } },
          {
            "p": {
              "text": "$100",
              "style": { "text-align": "right" }
            }
          }
        ]
      },
      {
        "type": "td",
        "cells": [
          { "p": { "text": "February" } },
          {
            "p": {
              "text": "$80",
              "style": { "text-align": "right" }
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

/* Example JSON:
{
  "caption": {
    "p": {
      "text": "My savings",
      "style": {
        "text-align": "right"
      }
    }
  }
}
*/
data class TableCaption(
    override val name: String = "caption",
    @SerialName("p")
    val textBlock: TextBlock
) : Block, WithJsonName

/* Example JSON:
{
  "tr": {
    "type": "th",
    "cells": [
      { "p": { "text": "Month" } },
      { "p": { "text": "Savings" } }
    ]
  }
}

{
  "tr": {
    "type": "td",
    "cells": [
      { "p": { "text": "January" } },
      {
        "p": {
          "text": "$100",
          "style": {
            "text-align": "right"
          }
        }
      }
    ]
  }
}
*/
data class TableRow(
    override val name: String = "tr",
    @SerialName("type")
    val type: Type,
    @SerialName("cells")
    val cells: List<Cell>
) : Block, WithJsonName {
    @Serializable
    enum class Type {
        @SerialName("th")
        HEADER,
        @SerialName("td")
        DATA
    }

    sealed class Cell {
        data class Header(val cell: TableHeaderCell)

        data class Data(val cell: TableDataCell)
    }
}

data class TableHeaderCell(
    override val name: String = "th",
    val value: Value,
    @SerialName("style")
    val style: Style?
) : Block, WithJsonName {
    sealed class Value {
        data class Text(val text: TextBlock) : Value()

        data class Code(val code: CodeBlock) : Value()
    }
}

data class TableDataCell(
    override val name: String = "td",
    val value: Value,
    @SerialName("style")
    val style: Style?
) : Block, WithJsonName {
    sealed class Value {
        data class Text(val text: TextBlock) : Value()

        data class Code(val code: CodeBlock) : Value()

        data class Image(val image: ImageBlock) : Value()

        data class PreformattedText(val preformattedText: PreformattedTextBlock) : Value()

        data class List(val list: ListBlock) : Value()
    }
}

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
    "p": {
      "text": "Defines a caption for the figure."
    }
  }
}
*/
data class FigureCaptionBlock(
    override val name: String = "figcaption",
    @SerialName("p")
    val textBlock: TextBlock
) : Block, WithJsonName

/* Example JSON:
{
  "blockquote": {
    "p": {
      "text": "Defines a blockquote."
    }
  }
}
*/
data class BlockquoteBlock(
    override val name: String = "blockquote",
    val value: Value
) : Block, WithJsonName {
    sealed class Value {
        data class Text(val text: TextBlock) : Value()

        data class List(val list: ListBlock) : Value()
    }
}

/* Example JSON:
{
  "details": {
    "summary": "Epcot Center",
    "items": [
      {
        "p": {
          "text": "Epcot is a theme park at Walt Disney"
        }
      }
    ]
  }
}
*/
data class DetailsBlock(
    override val name: String = "details",
    @SerialName("summary")
    val summary: String,
    @SerialName("items")
    val items: List<Item>
) : Block, WithJsonName {
    sealed class Item {
        data class Text(val text: TextBlock) : Item()

        data class Code(val code: CodeBlock) : Item()

        data class Image(val image: ImageBlock) : Item()

        data class PreformattedText(val preformattedText: PreformattedTextBlock) : Item()

        data class List(val list: ListBlock) : Item()
    }
}

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
    val srcUrl: String?
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