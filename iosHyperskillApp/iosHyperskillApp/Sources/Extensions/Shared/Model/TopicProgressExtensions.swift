import Foundation
import shared

extension TopicProgress {
    convenience init(
        id: String = UUID().uuidString,
        vid: String = UUID().uuidString,
        stagePosition: Int32? = nil,
        repeatedCount: Int32? = nil,
        isCompleted: Bool = false,
        isSkipped: Bool = false,
        capacity: Float = 0,
        isInCurrentTrack: Bool = false
    ) {
        self.init(
            id: id,
            vid: vid,
            stagePosition: stagePosition != nil ? KotlinInt(value: stagePosition.require()) : nil,
            repeatedCount: repeatedCount != nil ? KotlinInt(value: repeatedCount.require()) : nil,
            isCompleted: isCompleted,
            isSkipped: isSkipped,
            capacity: KotlinFloat(value: capacity),
            isInCurrentTrack: isInCurrentTrack
        )
    }
}
