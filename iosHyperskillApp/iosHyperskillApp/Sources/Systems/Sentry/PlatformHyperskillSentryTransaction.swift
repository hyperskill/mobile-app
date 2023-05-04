import Foundation
import Sentry
import shared

final class PlatformHyperskillSentryTransaction: HyperskillSentryTransaction {
    let span: Span

    init(span: Span, name: String, operation: String, tags: [String: String]) {
        self.span = span
        super.init(name: name, operation: operation, tags_: tags)
    }
}
