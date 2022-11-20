import Foundation

struct TrackViewData {
    let coverSource: String?
    let name: String
    let learningRole: String

    let currentTimeToCompleteText: String?

    let completedGraduateProjectsCountText: String?

    let completedTopicsText: String?
    let completedTopicsProgress: Float

    let capstoneTopicsText: String?
    let capstoneTopicsProgress: Float

    // About
    let ratingText: String?
    let allTimeToCompleteText: String?
    let projectsCountText: String?
    let topicsCountText: String?
    let description: String
    let webActionButtonText: String

    // Theory to discover next
    let topicsToLearnNext: [TheoryTopic]

    struct TheoryTopic: Identifiable {
        let id: Int64
        let title: String
    }
}
