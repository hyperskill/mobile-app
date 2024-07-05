package org.hyperskill.app.core.domain

enum class BuildVariant(val value: String) {
    DEBUG("debug"),
    RELEASE("release"),
    INTERNAL_RELEASE("internalRelease");

    companion object {
        fun getByValue(value: String): BuildVariant? =
            entries.firstOrNull { it.value == value }
    }

    fun isDebug(): Boolean =
        when (this) {
            DEBUG -> true
            RELEASE, INTERNAL_RELEASE -> false
        }
}