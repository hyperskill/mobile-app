package org.hyperskill.app.core.domain

enum class BuildVariant {
    DEBUG,
    RELEASE;

    fun isDebug(): Boolean =
        when (this) {
            DEBUG -> true
            RELEASE -> false
        }
}