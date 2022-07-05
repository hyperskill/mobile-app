import Foundation

struct TrackViewData {
    let iconImageName: String
    let name: String
    let role: String

    let timeToCompleteTrackTitle: String
    let timeToCompleteTrackSubtitle: String

    let completedGraduateProjectTitle: String?
    let completedGraduateProjectSubtitle: String

    let completedTopicsTitle: String
    let completedTopicsProgress: Float
    let completedTopicsSubtitle: String

    let appliedCoreTopicsByCompletingProjectStagesTitle: String
    let appliedCoreTopicsByCompletingProjectStagesProgress: Float
    let appliedCoreTopicsByCompletingProjectStagesSubtitle: String

    let rating: String?
    let timeToComplete: String?
    let projectsCount: String?
    let topicsCount: String?
    let description: String
    let buttonText: String
}

extension TrackViewData {
    static var placeholder: TrackViewData {
        TrackViewData(
            iconImageName: Images.TabBar.track,
            name: "Python for Beginners",
            role: "Learning now",
            timeToCompleteTrackTitle: "~ 56 h",
            timeToCompleteTrackSubtitle: "Time to complete the track",
            completedGraduateProjectTitle: "1",
            completedGraduateProjectSubtitle: "Completed graduate project ",
            completedTopicsTitle: "48 / 149",
            completedTopicsProgress: 0.322_147_651,
            completedTopicsSubtitle: "Completed topics",
            appliedCoreTopicsByCompletingProjectStagesTitle: "0 / 138",
            appliedCoreTopicsByCompletingProjectStagesProgress: 0,
            appliedCoreTopicsByCompletingProjectStagesSubtitle: "Applied core topics by completing project stages",
            rating: "4.7",
            timeToComplete: "104 hours",
            projectsCount: "20 projects",
            topicsCount: "220 topics",
            description: """
You've never tried programming and would like to build a solid foundation? This track is perfect for you! While
completing this track you’ll create 5 simple projects. We’ll start with the basics and will use a lot of examples to
really explain the possibilities of...
""",
            buttonText: "Keep your progress in web ↗"
        )
    }
}
