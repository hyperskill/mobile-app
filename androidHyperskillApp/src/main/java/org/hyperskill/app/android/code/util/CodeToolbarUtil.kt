package org.hyperskill.app.android.code.util

object CodeToolbarUtil {
    fun mapToolbarSymbolToPrintable(symbol: String, indentSize: Int): String {
        return if (symbol.equals("tab", ignoreCase = true)) {
            " ".repeat(indentSize)
        } else {
            symbol
        }
    }
}
