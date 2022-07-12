import Foundation
import shared

final class TrackViewDataMapper {
    func mapTrackDataToViewData(track: Track, trackProgress: TrackProgress, studyPlan: StudyPlan?) -> TrackViewData {
        let currentTimeToCompleteText: String = {
            let hoursToComplete = Int(Float(studyPlan?.secondsToReachTrack ?? 0) / 3600.0)
            return "~ \(hoursToComplete) h"
        }()

        let completedGraduateProjectsCountText = track.capstoneTopicsCount > 0
            ? "\(trackProgress.completedCapstoneProjects.count)"
            : nil

        let completedTopicsText = "\(trackProgress.learnedTopicsCount) / \(track.topicsCount)"
        let completedTopicsProgress = track.topicsCount == 0
            ? 0
            : Float(trackProgress.learnedTopicsCount) / Float(track.topicsCount)

        let capstoneTopicsText = track.capstoneTopicsCount > 0
            ? "\(trackProgress.appliedCapstoneTopicsCount) / \(track.capstoneTopicsCount)"
            : nil
        let capstoneTopicsProgress = track.capstoneTopicsCount > 0
            ? Float(trackProgress.appliedCapstoneTopicsCount) / Float(track.capstoneTopicsCount)
            : 0

        let ratingText: String? = {
            guard let usefulness = trackProgress.usefulness else {
                return nil
            }

            return Formatter.averageRating(usefulness.floatValue)
        }()

        let allTimeToCompleteText: String = {
            let hours = Int(track.secondsToComplete / 3600)
            return "\(hours) hours"
        }()

        let projectsCountText: String? = {
            guard !track.projects.isEmpty else {
                return nil
            }

            return "\(track.projects.count) projects"
        }()

        let topicsCountText = "\(track.topicsCount) topics"

        return TrackViewData(
            coverSource: track.cover?.trimmedNonEmptyOrNil(),
            name: track.title,
            learningRole: Strings.Track.learningNow,
            currentTimeToCompleteText: currentTimeToCompleteText,
            completedGraduateProjectsCountText: completedGraduateProjectsCountText,
            completedTopicsText: completedTopicsText,
            completedTopicsProgress: completedTopicsProgress,
            capstoneTopicsText: capstoneTopicsText,
            capstoneTopicsProgress: capstoneTopicsProgress,
            ratingText: ratingText,
            allTimeToCompleteText: allTimeToCompleteText,
            projectsCountText: projectsCountText,
            topicsCountText: topicsCountText,
            description: track.description_.trimmed(),
            webActionButtonText: Strings.Track.continueInWebButton
        )
    }
}
