package org.hyperskill.app.android.code.presentation.autocomplete

import android.content.Context
import org.hyperskill.app.android.R

class AutocompleteContainer(context: Context) {
    private val autocomplete = hashMapOf(
        "cpp" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_cpp)),
        "cs" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_cs)),
        "css" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_css)),
        "html" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_html)),
        "java" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_java)),
        "js" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_js)),
        "php" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_php)),
        "py" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_py)),
        "rb" to AutocompleteDictionary(context.resources.getStringArray(R.array.autocomplete_words_rb)),
        "sql" to AutocompleteDictionary(
            context.resources.getStringArray(R.array.autocomplete_words_sql),
            isCaseSensitive = false
        )
    )

    fun getAutoCompleteForLangAndPrefix(lang: String, prefix: String): List<String> =
        autocomplete[lang]?.getAutocompleteForPrefix(prefix) ?: emptyList()
}