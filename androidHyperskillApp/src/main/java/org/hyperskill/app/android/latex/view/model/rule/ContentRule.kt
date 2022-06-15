package org.hyperskill.app.android.latex.view.model.rule

interface ContentRule {
    /**
     * Processes given [content] and return new one
     */
    fun process(content: String): String
}