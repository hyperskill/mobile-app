package org.hyperskill.app.core.domain

/**
 * Logs error, then throws it
 */
internal fun throwError(throwable: Throwable): Nothing {
    // you can add logging here
    throw throwable
}