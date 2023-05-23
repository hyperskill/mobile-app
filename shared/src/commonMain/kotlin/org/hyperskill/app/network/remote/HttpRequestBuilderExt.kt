package org.hyperskill.app.network.remote

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

/**
 * Appends a single URL query parameter of "ids" with a specific [ids] if the value is not empty.
 *
 * @param ids List of ids to append to the URL query.
 * @param transform A function that transforms a single id to a [CharSequence].
 */
internal fun HttpRequestBuilder.parameterIds(
    ids: List<Long>,
    transform: ((Long) -> CharSequence)? = null
) =
    parameter(
        key = "ids",
        value = if (ids.isEmpty()) {
            null
        } else {
            ids.joinToString(separator = ",", transform = transform)
        }
    )