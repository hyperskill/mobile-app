package org.hyperskill.app.core.domain.model

sealed class SwiftyResult<out S, out F : Throwable> {
    data class Success<out S, out F : Throwable>(val value: S) : SwiftyResult<S, F>()
    data class Failure<out S, out F : Throwable>(val failure: F) : SwiftyResult<S, F>()
}

internal fun <S, F : Throwable> SwiftyResult<S, F>.toKotlinResult(): Result<S> =
    when (this) {
        is SwiftyResult.Success -> Result.success(value)
        is SwiftyResult.Failure -> Result.failure(failure)
    }