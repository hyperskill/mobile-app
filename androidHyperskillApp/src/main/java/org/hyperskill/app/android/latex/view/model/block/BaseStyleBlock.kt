package org.hyperskill.app.android.latex.view.model.block

import androidx.annotation.ColorInt

class BaseStyleBlock(
    isNightMode: Boolean,
    fontPath: String,
    @ColorInt
    private val textColor: Int
) : ContentBlock {
    override val header: String = """
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/alt.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/altcontent.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/fonts.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/highlight.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/hljs.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/icons.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/step.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/variables.css"/>
        <link rel="stylesheet" type="text/css" href="file:///android_asset/css/wysiwyg.css"/>
        
        <style>
            @font-face {
                font-family: 'Roboto';
                src: url("$fontPath")
            }
        
            html{-webkit-text-size-adjust: 100%%;}
            body{
                font-family:'Roboto', Helvetica, sans-serif; line-height:1.6em;
                color: ${formatRGBA(textColor)};
            }
            h1{font-size: 22px; font-family:'Roboto', Helvetica, sans-serif; line-height:1.6em;text-align: center;}
            h2{font-size: 19px; font-family:'Roboto', Helvetica, sans-serif; line-height:1.6em;text-align: center;}
            h3{font-size: 16px; font-family:'Roboto', Helvetica, sans-serif; line-height:1.6em;text-align: center;}
            img { max-width: 100%%; }
        </style>
    """.trimIndent()

    private fun formatRGBA(@ColorInt color: Int): String {
        val b = color and 0xFF
        val g = (color shr 8) and 0xFF
        val r = (color shr 16) and 0xFF
        val a = (color shr 24) and 0xFF

        return "rgba($r, $g, $b, ${a.toDouble() / 0xFF})"
    }
}