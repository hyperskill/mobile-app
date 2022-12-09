import Foundation
import Sentry
import shared

final class PlatformHyperskillSentryTransaction: HyperskillSentryTransaction {
    let span: Span

    init(span: Span, name: String, operation_ operation: String) {
        self.span = span
        super.init(name: name, operation_: operation)
    }
}
