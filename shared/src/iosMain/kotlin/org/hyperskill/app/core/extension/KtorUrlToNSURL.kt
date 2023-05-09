package org.hyperskill.app.core.extension

import io.ktor.http.Url
import platform.Foundation.NSURL

fun Url.toNSURL(): NSURL =
    NSURL(string = toString())