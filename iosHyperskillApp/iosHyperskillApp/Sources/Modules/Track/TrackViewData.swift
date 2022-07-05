import Foundation

struct TrackViewData {
    let iconImageName: String
    let name: String
    let role: String

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
