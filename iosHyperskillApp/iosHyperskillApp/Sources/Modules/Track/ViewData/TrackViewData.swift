import Foundation

struct TrackViewData {
    // MARK: Header

    let coverSource: String?
    let name: String
    let learningRole: String

    // MARK: Progress

    let currentTimeToCompleteText: String?

    let completedGraduateProjectsCountText: String?

    let completedTopicsText: String?
    let completedTopicsProgress: Float

    let capstoneTopicsText: String?
    let capstoneTopicsProgress: Float

    // MARK: About

    let ratingText: String?
    let allTimeToCompleteText: String?
    let projectsCountText: String?
    let topicsCountText: String?
    let description: String
    let webActionButtonText: String
}
