package org.hyperskill.app.android.code.injection

import org.hyperskill.app.android.code.presentation.autocomplete.AutocompleteContainer
import org.hyperskill.app.android.code.highlight.prettify.PrettifyParser
import org.hyperskill.app.android.code.view.widget.CodeAnalyzer

interface PlatformCodeEditorComponent {
    val prettifyParser: PrettifyParser
    val autocompleteContainer: AutocompleteContainer
    val codeAnalyzer: CodeAnalyzer
}