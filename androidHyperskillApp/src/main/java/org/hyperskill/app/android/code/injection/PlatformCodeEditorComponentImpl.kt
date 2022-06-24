package org.hyperskill.app.android.code.injection

import android.content.Context
import org.hyperskill.app.android.code.presentation.autocomplete.AutocompleteContainer
import org.hyperskill.app.android.code.highlight.prettify.PrettifyParser
import org.hyperskill.app.android.code.view.widget.CodeAnalyzer

class PlatformCodeEditorComponentImpl(
    private val context: Context
) : PlatformCodeEditorComponent {
    override val prettifyParser: PrettifyParser
        get() = PrettifyParser()

    override val autocompleteContainer: AutocompleteContainer
        get() = AutocompleteContainer(context)

    override val codeAnalyzer: CodeAnalyzer
        get() = CodeAnalyzer(autocompleteContainer)
}