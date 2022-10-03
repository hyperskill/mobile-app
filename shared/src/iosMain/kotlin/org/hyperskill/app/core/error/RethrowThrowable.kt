package org.hyperskill.app.core.error

@Throws(Throwable::class)
fun rethrow(exception: Throwable): Nothing {
    throw exception
}
