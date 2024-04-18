import Foundation
import shared

extension TopicProgress {
    convenience init(
        vid: String = UUID().uuidString,
        isCompleted: Bool = false,
        isSkipped: Bool = false,
        capacity: Float = 0
    ) {
        self.init(
            vid: vid,
            isCompleted: isCompleted,
            isSkipped: isSkipped,
            capacity: KotlinFloat(value: capacity)
        )
    }
}
