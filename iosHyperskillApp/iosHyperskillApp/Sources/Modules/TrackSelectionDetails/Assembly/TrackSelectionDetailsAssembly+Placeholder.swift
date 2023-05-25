import Foundation
import shared

// swiftlint:disable all

#if DEBUG
extension TrackSelectionDetailsAssembly {
    static func makePlaceholder() -> TrackSelectionDetailsAssembly {
        TrackSelectionDetailsAssembly(
            trackWithProgress: TrackWithProgress(
                track: Track(
                    description: "",
                    isBeta: true,
                    isFree: true,
                    id: 1,
                    isCompleted: false,
                    canIssueCertificate: true,
                    projectsByLevel: ProjectsByLevel(nightmare: nil, medium: nil, easy: nil, hard: nil),
                    results: """
This track will guide you on your path of mastering Python, one of the much-in-demand languages in today's environment. Known for its straightforward syntax, Python is easy to learn and use, which accounts for its popularity and makes its community grow every day. This track is dedicated to core Python skills that will give you a solid base and allow you to pursue any further direction, be it Backend Development or Data Science.
""",
                    secondsToComplete: KotlinFloat(value: 896400),
                    title: "Kotlin Core",
                    topicsCount: 152,
                    cover: nil,
                    careers: "",
                    projects: [],
                    betaProjects: [],
                    progressId: "",
                    isPublic: true,
                    capstoneProjects: [],
                    capstoneTopicsCount: 0,
                    providerId: 2,
                    topicProviders: [],
                    progress: nil
                ),
                trackProgress: TrackProgress(
                    id: "",
                    vid: "",
                    clarity: nil,
                    funMeasure: nil,
                    usefulness: nil,
                    completedProjects: [],
                    completedCapstoneProjects: [],
                    appliedCapstoneTopicsCount: 0,
                    isCompleted: false,
                    learnedTopicsCount: 0,
                    skippedTopicsCount: 0,
                    rank: 0
                )
            ),
            isTrackSelected: false
        )
    }
}
#endif
