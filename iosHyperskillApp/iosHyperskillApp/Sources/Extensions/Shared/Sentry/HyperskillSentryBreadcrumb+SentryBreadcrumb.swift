import Foundation
import Sentry
import shared

extension HyperskillSentryBreadcrumb {
    var sentryBreadcrumb: Breadcrumb {
        let breadcrumb = Breadcrumb()

        breadcrumb.category = category
        breadcrumb.message = message
        breadcrumb.level = level.sentryLevel
        breadcrumb.data = data

        return breadcrumb
    }
}
