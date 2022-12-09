package org.hyperskill.app.android.sentry.extensions

import io.sentry.Breadcrumb
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumb

fun Breadcrumb(hyperskillSentryBreadcrumb: HyperskillSentryBreadcrumb): Breadcrumb =
    Breadcrumb().apply {
        category = hyperskillSentryBreadcrumb.category
        message = hyperskillSentryBreadcrumb.message
        level = hyperskillSentryBreadcrumb.level.toSentryLevel()
        hyperskillSentryBreadcrumb.data?.forEach { setData(it.key, it.value) }
    }