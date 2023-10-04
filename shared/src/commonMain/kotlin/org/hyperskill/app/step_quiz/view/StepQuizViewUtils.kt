package org.hyperskill.app.step_quiz.view

import org.hyperskill.app.step.domain.model.Step

private const val LANGUAGE_PATTERN = "([^0-9]+)([0-9]*)"

private val DISPLAY_LANGS = mapOf(
    "javasript" to "JavaScript"
)

val Step.displayLanguage: String?
    get() {
        val languageString =
            block.options.language?.takeIf(String::isNotEmpty)
                ?: block.options.codeTemplates?.keys?.firstOrNull()
                ?: return null
        if (languageString in DISPLAY_LANGS) {
            return DISPLAY_LANGS[languageString]
        }
        val match = LANGUAGE_PATTERN.toRegex().find(languageString)
        return if (match != null) {
            val (langName, version) = match.destructured
            val displayName = DISPLAY_LANGS[langName] ?: langName.capitalize()
            if (version.isNotEmpty()) {
                "$displayName $version"
            } else {
                displayName
            }
        } else {
            languageString.capitalize()
        }
    }

private fun String.capitalize(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
