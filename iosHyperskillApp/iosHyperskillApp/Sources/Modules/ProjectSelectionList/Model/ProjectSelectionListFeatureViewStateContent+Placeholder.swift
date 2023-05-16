import Foundation
import shared

#if DEBUG
extension ProjectSelectionListFeatureViewStateContent {
    static var placeholder: ProjectSelectionListFeatureViewStateContent {
        let selectedProject = ProjectSelectionListFeature.ProjectListItem(
            id: 1,
            title: "Last Pencil",
            averageRating: 4.7,
            level: .nightmare,
            formattedTimeToComplete: "12 hours",
            isGraduate: true,
            isBestRated: true,
            isIdeRequired: true,
            isFastestToComplete: true,
            isCompleted: true
        )

        let recommendedProjects: [ProjectSelectionListFeature.ProjectListItem] = [
            .init(
                id: 2,
                title: "Simple Chatty Bot (Python)",
                averageRating: 4.6,
                level: .medium,
                formattedTimeToComplete: "24 hours",
                isGraduate: false,
                isBestRated: false,
                isIdeRequired: false,
                isFastestToComplete: false,
                isCompleted: false
            ),
            .init(
                id: 3,
                title: "Honest Calculator",
                averageRating: 4.5,
                level: .easy,
                formattedTimeToComplete: "136 hours",
                isGraduate: false,
                isBestRated: true,
                isIdeRequired: true,
                isFastestToComplete: false,
                isCompleted: false
            )
        ]

        let projectsByLevel: [ProjectLevel: [ProjectSelectionListFeature.ProjectListItem]] = [
            .easy: [
                .init(
                    id: 4,
                    title: "Simple Chatty Bot (Python)",
                    averageRating: 4.4,
                    level: .easy,
                    formattedTimeToComplete: "24 hours",
                    isGraduate: false,
                    isBestRated: false,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                ),
                .init(
                    id: 5,
                    title: "Honest Calculator",
                    averageRating: 4.3,
                    level: .easy,
                    formattedTimeToComplete: "136 hours",
                    isGraduate: false,
                    isBestRated: true,
                    isIdeRequired: true,
                    isFastestToComplete: false,
                    isCompleted: false
                )
            ],
            .medium: [
                .init(
                    id: 6,
                    title: "Bulls and Cows",
                    averageRating: 4.2,
                    level: .medium,
                    formattedTimeToComplete: "24 hours",
                    isGraduate: false,
                    isBestRated: false,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                ),
                .init(
                    id: 7,
                    title: "Graph-Algorithms Visualizer",
                    averageRating: 4.1,
                    level: .medium,
                    formattedTimeToComplete: "136 hours",
                    isGraduate: false,
                    isBestRated: false,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                )
            ],
            .hard: [
                .init(
                    id: 8,
                    title: "Simple Chatty Bot (Python)",
                    averageRating: 4.0,
                    level: .hard,
                    formattedTimeToComplete: "24 hours",
                    isGraduate: false,
                    isBestRated: false,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                ),
                .init(
                    id: 9,
                    title: "Last Pencil",
                    averageRating: 4.1,
                    level: .hard,
                    formattedTimeToComplete: "136 hours",
                    isGraduate: true,
                    isBestRated: true,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                )
            ],
            .nightmare: [
                .init(
                    id: 10,
                    title: "Simple Chatty Bot (Python)",
                    averageRating: 3.9,
                    level: .nightmare,
                    formattedTimeToComplete: "1000 hours",
                    isGraduate: true,
                    isBestRated: true,
                    isIdeRequired: false,
                    isFastestToComplete: false,
                    isCompleted: false
                )
            ]
        ]

        return ProjectSelectionListFeatureViewStateContent(
            trackIcon: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            formattedTitle: "Select project in track Python Core",
            selectedProject: selectedProject,
            recommendedProjects: recommendedProjects,
            projectsByLevel: projectsByLevel,
            isProjectSelectionLoadingShowed: false
        )
    }
}
#endif
