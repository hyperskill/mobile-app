import Foundation
import shared

extension Kotlinx_datetimeInstant {
    var asSwiftDate: Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }

    static func fromSwiftDate(_ date: Date) -> Kotlinx_datetimeInstant {
        Kotlinx_datetimeInstant.companion.fromEpochMilliseconds(
            epochMilliseconds: Int64(date.timeIntervalSince1970) * 1000
        )
    }

    static func fromSwiftCurrentDate() -> Kotlinx_datetimeInstant { fromSwiftDate(.init()) }
}
