package org.hyperskill.app.android.latex.view.model.block

import org.hyperskill.app.android.latex.view.model.Settings

interface ContentBlock {
    val header: String
        get() = ""

    val preBody: String
        get() = ""

    val postBody: String
        get() = ""

    val settings: Settings
        get() = Settings.DEFAULT_SETTINGS

    fun isEnabled(content: String): Boolean =
        true
}