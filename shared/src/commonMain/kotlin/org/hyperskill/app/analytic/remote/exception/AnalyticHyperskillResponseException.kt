package org.hyperskill.app.analytic.remote.exception

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class AnalyticHyperskillResponseException(response: HttpResponse, cachedResponseText: String) :
    ResponseException(response, cachedResponseText) {
    override val message: String = "AnalyticHyperskillResponseException: " +
        "${response.call.request.url}. " +
        "Status: ${response.status}. Text: \"$cachedResponseText\""
}