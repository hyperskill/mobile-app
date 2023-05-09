package org.hyperskill.app.android.latex.view.model

data class Settings(val allowUniversalAccessFromFileURLs: Boolean) {
    companion object {
        val DEFAULT_SETTINGS = Settings(allowUniversalAccessFromFileURLs = false)
    }

    operator fun plus(other: Settings): Settings =
        Settings(this.allowUniversalAccessFromFileURLs || other.allowUniversalAccessFromFileURLs)
}