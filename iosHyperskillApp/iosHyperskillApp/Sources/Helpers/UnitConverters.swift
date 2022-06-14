import Foundation

enum UnitConverters {
    enum Nanosecond {
        static func from(second: TimeInterval) -> UInt64 {
            UInt64(second * 1_000_000_000)
        }
    }
}
