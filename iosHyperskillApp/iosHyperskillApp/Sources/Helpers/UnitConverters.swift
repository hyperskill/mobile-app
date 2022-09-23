import Foundation

enum UnitConverters {
    enum Hour {
        static func from(seconds: TimeInterval, roundingRule: FloatingPointRoundingRule) -> Int {
            Int((seconds / .oneHour).rounded(roundingRule))
        }
    }

    enum Minute {
        static func from(seconds: TimeInterval, roundingRule: FloatingPointRoundingRule) -> Int {
            Int((seconds / .oneMinute).rounded(roundingRule))
        }
    }

    enum Nanosecond {
        static func from(second: TimeInterval) -> UInt64 {
            UInt64(second * 1_000_000_000)
        }
    }
}
