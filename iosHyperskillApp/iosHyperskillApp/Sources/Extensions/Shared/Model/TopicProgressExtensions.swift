import Foundation
import shared

extension TopicProgress {
    convenience init(
        id: String = UUID().uuidString,
        vid: String = UUID().uuidString,
        isCompleted: Bool = false,
        isSkipped: Bool = false,
        capacity: Float = 0
    ) {
        self.init(
            id: id,
            vid: vid,
            isCompleted: isCompleted,
            isSkipped: isSkipped,
            capacity: KotlinFloat(value: capacity)
        )
    }
}
