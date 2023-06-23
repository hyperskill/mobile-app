package org.hyperskill.app.core.domain

enum class BuildVariant(val value: String) {
    DEBUG("debug"),
    RELEASE("release"),
    INTERNAL_RELEASE("internalRelease");

    companion object {
        private val VALUES: Array<BuildVariant> = values()

        fun getByValue(value: String): BuildVariant? =
            VALUES.firstOrNull { it.value == value }
    }

    fun isDebug(): Boolean =
        when (this) {
            DEBUG -> true
            RELEASE, INTERNAL_RELEASE -> false
        }
}