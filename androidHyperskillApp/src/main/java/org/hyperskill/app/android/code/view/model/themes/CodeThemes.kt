package org.hyperskill.app.android.code.view.model.themes

import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.ColorUtil

object CodeThemes {
    val Light = CodeTheme(
        name = HyperskillApp.getAppContext().getString(R.string.light_theme_name),
        syntax = CodeSyntax(
            plain = ColorUtil.getColorArgb(R.color.light_theme_plain),
            string = ColorUtil.getColorArgb(R.color.light_theme_string),
            keyword = ColorUtil.getColorArgb(R.color.light_theme_keyword),
            comment = ColorUtil.getColorArgb(R.color.light_theme_comment),
            type = ColorUtil.getColorArgb(R.color.light_theme_type),
            literal = ColorUtil.getColorArgb(R.color.light_theme_literal),
            punctuation = ColorUtil.getColorArgb(R.color.light_theme_punctuation),
            attributeName = ColorUtil.getColorArgb(R.color.light_theme_attribute_name),
            attributeValue = ColorUtil.getColorArgb(R.color.light_theme_attribute_value)
        ),
        background = ColorUtil.getColorArgb(R.color.light_theme_background),
        lineNumberBackground = ColorUtil.getColorArgb(R.color.light_theme_line_number_background),
        lineNumberText = ColorUtil.getColorArgb(R.color.light_theme_line_number_text),
        selectedLineBackground = ColorUtil.getColorArgb(R.color.light_theme_selected_line_background),
        lineNumberStroke = ColorUtil.getColorArgb(R.color.light_theme_line_number_stroke),
        bracketsHighlight = ColorUtil.getColorArgb(R.color.light_theme_brackets_highlight)
    )

    val GitHub = CodeTheme(
        name = HyperskillApp.getAppContext().getString(R.string.github_theme_name),
        syntax = CodeSyntax(
            plain = ColorUtil.getColorArgb(R.color.github_theme_plain),
            string = ColorUtil.getColorArgb(R.color.github_theme_string),
            comment = ColorUtil.getColorArgb(R.color.github_theme_comment),
            type = ColorUtil.getColorArgb(R.color.github_theme_type),
            literal = ColorUtil.getColorArgb(R.color.github_theme_literal),
            attributeName = ColorUtil.getColorArgb(R.color.github_theme_attribute_name),
            attributeValue = ColorUtil.getColorArgb(R.color.github_theme_attribute_value)
        ),
        background = ColorUtil.getColorArgb(R.color.github_theme_background),
        lineNumberBackground = ColorUtil.getColorArgb(R.color.github_theme_line_number_background),
        lineNumberText = ColorUtil.getColorArgb(R.color.github_theme_line_number_text),
        selectedLineBackground = ColorUtil.getColorArgb(R.color.github_theme_selected_line_background),
        lineNumberStroke = ColorUtil.getColorArgb(R.color.github_theme_line_number_stroke)
    )

    val TomorrowNight = CodeTheme(
        name = HyperskillApp.getAppContext().getString(R.string.tomorrow_night_theme_name),
        syntax = CodeSyntax(
            plain = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_plain),
            string = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_string),
            keyword = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_keyword),
            comment = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_comment),
            type = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_type),
            literal = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_literal),
            tag = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_tag),
            attributeName = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_attribute_name),
            attributeValue = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_attribute_value),
            declaration = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_declaration)
        ),
        background = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_background),
        lineNumberBackground = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_background),
        lineNumberText = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_text),
        selectedLineBackground = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_selected_line_background),
        lineNumberStroke = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_stroke)
    )

    val TranquilHeart = CodeTheme(
        name = HyperskillApp.getAppContext().getString(R.string.tomorrow_night_theme_name),
        syntax = CodeSyntax(
            plain = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_plain),
            string = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_string),
            keyword = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_keyword),
            comment = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_comment),
            type = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_type),
            literal = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_literal),
            tag = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_tag),
            attributeName = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_attribute_name),
            attributeValue = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_attribute_value),
            declaration = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_declaration)
        ),
        background = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_background),
        lineNumberBackground = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_background),
        lineNumberText = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_text),
        selectedLineBackground = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_selected_line_background),
        lineNumberStroke = ColorUtil.getColorArgb(R.color.tomorrow_night_theme_line_number_stroke)
    )
}