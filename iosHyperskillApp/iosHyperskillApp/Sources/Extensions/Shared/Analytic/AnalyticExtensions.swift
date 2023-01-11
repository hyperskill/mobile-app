import Foundation
import shared

extension Analytic {
    func reportEvent(event: AnalyticEvent) {
        reportEvent(event: event, forceReportEvent: false)
    }
}

extension AnalyticInteractor {
    func logEvent(event: AnalyticEvent, completionHandler: ((Error?) -> Void)? = nil) {
        logEvent(event: event, forceLogEvent: false, completionHandler: completionHandler ?? { _ in })
    }
}
