package org.hyperskill.app.core.domain.model

sealed class SwiftyResult<out S, out F : Throwable> {
    data class Success<out S, out F : Throwable>(val value: S) : SwiftyResult<S, F>()
    data class Failure<out S, out F : Throwable>(val failure: F) : SwiftyResult<S, F>()

    internal fun toKotlinResult(): Result<S> =
        when (this) {
            is Success -> Result.success(value)
            is Failure -> Result.failure(failure)
        }
}