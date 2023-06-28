import Foundation
import shared

@available(*, deprecated, message: "Legacy class")
final class TrackViewDataMapper {
    private let formatter: Formatter

    init(formatter: Formatter) {
        self.formatter = formatter
    }

    func mapTrackDataToViewData(
        track: Track,
        trackProgress: TrackProgress,
        studyPlan: StudyPlan?
    ) -> TrackViewData {
        let currentTimeToCompleteText: String? = {
            guard let studyPlan = studyPlan else {
                return nil
            }

            if studyPlan.hoursToReachTrack > 0 {
                return "~ \(studyPlan.hoursToReachTrack) h"
            }

            if studyPlan.minutesToReachTrack > 0 {
                return "~ \(studyPlan.minutesToReachTrack) m"
            }

            return nil
        }()

        let completedGraduateProjectsCountText = track.capstoneProjects.isEmpty
            ? nil
            : "\(trackProgress.completedCapstoneProjects.count)"

        let completedTopicsText = track.topicsCount > 0
            ? "\(trackProgress.completedTopics) / \(track.topicsCount)"
            : nil
        let completedTopicsProgress = track.topicsCount == 0
            ? 0
            : Float(trackProgress.completedTopics) / Float(track.topicsCount)

        let capstoneTopicsText = track.capstoneTopicsCount > 0
            ? "\(trackProgress.appliedCapstoneTopicsCount) / \(track.capstoneTopicsCount)"
            : nil
        let capstoneTopicsProgress = track.capstoneTopicsCount > 0
            ? Float(trackProgress.appliedCapstoneTopicsCount) / Float(track.capstoneTopicsCount)
            : 0

        let ratingText: String? = {
            guard trackProgress.averageRating > 0 else {
                return nil
            }

            return Formatter.averageRating(trackProgress.averageRating, decimalPoints: 1)
        }()

        // Deprecated file
        let allTimeToCompleteText = String(
            formatter.hoursCount(Int(track.secondsToComplete?.doubleValue ?? 0.0) / 3600)
        )

        let projectsCountText: String? = {
            guard !track.projects.isEmpty else {
                return nil
            }

            return formatter.projectsCount(track.projects.count)
        }()

        let topicsCountText: String? = {
            guard track.topicsCount > 0 else {
                return nil
            }

            return formatter.topicsCount(track.topicsCount)
        }()

        return TrackViewData(
            coverSource: track.cover?.trimmedNonEmptyOrNil(),
            name: track.title,
            learningRole: Strings.Track.Header.learningNow,
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
            webActionButtonText: Strings.Track.About.continueInWebButton
        )
    }
}
