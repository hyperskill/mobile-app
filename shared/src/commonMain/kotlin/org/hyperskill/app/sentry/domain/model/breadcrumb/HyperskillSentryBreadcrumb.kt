package org.hyperskill.app.sentry.domain.model.breadcrumb

import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

/**
 * Represents a Sentry breadcrumb.
 *
 * Sentry uses breadcrumbs to create a trail of events that happened prior to an issue.
 * These events are very similar to traditional logs, but can record more rich structured data.
 *
 * @property message A string describing the event.
 * @property level The severity of an event.
 * @property data A key-value mapping of event metadata.
 * @constructor Creates breadcrumb with the given parameters.
 *
 * @param category The category of the event.
 *
 * @see HyperskillSentryBreadcrumbCategory
 * @see HyperskillSentryLevel
 */
class HyperskillSentryBreadcrumb(
    category: HyperskillSentryBreadcrumbCategory,
    val message: String,
    val level: HyperskillSentryLevel,
    val data: Map<String, Any>? = null
) {
    val category: String = category.stringValue
}