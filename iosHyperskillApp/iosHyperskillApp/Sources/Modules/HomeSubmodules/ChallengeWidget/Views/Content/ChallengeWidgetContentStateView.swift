import shared
import SwiftUI

extension ChallengeWidgetContentStateView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        var backgroundColor = Color(ColorPalette.surface)
    }
}

struct ChallengeWidgetContentStateView: View {
    private(set) var appearance = Appearance()

    let stateKs: ChallengeWidgetViewStateContentKs

    let onOpenDescriptionLink: (URL) -> Void
    let onDeadlineReloadTap: () -> Void
    let onCollectRewardTap: () -> Void

    var body: some View {
        buildBody()
    }

    @ViewBuilder
    @MainActor
    private func buildBody() -> some View {
        let _ = handleMagicLinkLoadingIndicatorVisibility()

        VStack(alignment: .leading, spacing: appearance.spacing) {
            ChallengeWidgetContentStateHeaderView(stateKs: stateKs)

            ChallengeWidgetContentStateDescriptionView(
                stateKs: stateKs,
                onOpenLink: onOpenDescriptionLink
            )
            .equatable()

            switch stateKs {
            case .announcement(let announcementData):
                ChallengeWidgetContentStateDeadlineView(
                    startsInState: announcementData.startsInState,
                    reloadAction: onDeadlineReloadTap
                )
            case .happeningNow(let happeningNowData):
                ChallengeWidgetContentStateProgressGridView(
                    progressStatuses: happeningNowData.progressStatuses
                )

                ChallengeWidgetContentStateDeadlineView(
                    completeInState: happeningNowData.completeInState,
                    reloadAction: onDeadlineReloadTap
                )
            case .completed(let completedData):
                ChallengeWidgetContentStateCollectRewardButton(
                    collectRewardButtonState: completedData.collectRewardButtonState,
                    action: onCollectRewardTap
                )
            case .partiallyCompleted(let partiallyCompletedData):
                ChallengeWidgetContentStateCollectRewardButton(
                    collectRewardButtonState: partiallyCompletedData.collectRewardButtonState,
                    action: onCollectRewardTap
                )
            case .ended:
                EmptyView()
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(appearance.backgroundColor)
        .addBorder()
    }

    @MainActor
    private func handleMagicLinkLoadingIndicatorVisibility() {
        if ChallengeWidgetViewStateKt.isLoadingMagicLink(stateKs.sealed) {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismissWithDelay()
        }
    }
}

extension ChallengeWidgetContentStateView: Equatable {
    static func == (lhs: ChallengeWidgetContentStateView, rhs: ChallengeWidgetContentStateView) -> Bool {
        lhs.stateKs == rhs.stateKs
    }
}

// MARK: - Preview -

#if DEBUG
#Preview("Announcement") {
    let headerData = ChallengeWidgetViewStateContentHeaderData(
        title: "Advent Streak Challenge",
        description: """
Get ready to push your limits! Thrilling daily programming competition <a href="https://github.com/">What to Expect</a>.
""",
        formattedDurationOfTime: "6 Oct - 12 Oct"
    )
    return ScrollView {
        VStack {
            ChallengeWidgetContentStateView(
                stateKs: .announcement(
                    .init(
                        headerData: headerData,
                        startsInState: ChallengeWidgetViewStateContentAnnouncementStartsInStateTimeRemaining(
                            title: "Starts in",
                            subtitle: "6 hrs 27 mins"
                        )
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
            ChallengeWidgetContentStateView(
                stateKs: .announcement(
                    .init(
                        headerData: headerData,
                        startsInState: ChallengeWidgetViewStateContentAnnouncementStartsInStateDeadline()
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}

#Preview("Happening Now") {
    let headerData = ChallengeWidgetViewStateContentHeaderData(
        title: "Advent Streak Challenge",
        description: """
The challenge awaits! Thrilling daily programming competition designed <a href="https://github.com/">Read the rules</a>
""",
        formattedDurationOfTime: "6 Oct - 12 Oct"
    )
    return ScrollView {
        VStack {
            ChallengeWidgetContentStateView(
                stateKs: .happeningNow(
                    .init(
                        headerData: headerData,
                        completeInState: ChallengeWidgetViewStateContentHappeningNowCompleteInStateTimeRemaining(
                            title: "Complete in",
                            subtitle: "6 hrs 27 mins"
                        ),
                        progressStatuses: ChallengeWidgetViewStateContentHappeningNow.ProgressStatus.placeholder
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
            ChallengeWidgetContentStateView(
                stateKs: .happeningNow(
                    .init(
                        headerData: headerData,
                        completeInState: ChallengeWidgetViewStateContentHappeningNowCompleteInStateDeadline(),
                        progressStatuses: ChallengeWidgetViewStateContentHappeningNow.ProgressStatus.placeholder
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
            ChallengeWidgetContentStateView(
                stateKs: .happeningNow(
                    .init(
                        headerData: headerData,
                        completeInState: ChallengeWidgetViewStateContentHappeningNowCompleteInStateEmpty(),
                        progressStatuses: ChallengeWidgetViewStateContentHappeningNow.ProgressStatus.placeholder
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}

#Preview("Completed") {
    let headerData = ChallengeWidgetViewStateContentHeaderData(
        title: "Advent Streak Challenge",
        description: "Well done, challenge completed!",
        formattedDurationOfTime: "6 Oct - 12 Oct"
    )
    return ScrollView {
        VStack {
            ChallengeWidgetContentStateView(
                stateKs: .completed(
                    .init(
                        headerData: headerData,
                        collectRewardButtonState: ChallengeWidgetViewStateContentCollectRewardButtonStateVisible(
                            title: "Collect Reward"
                        ),
                        isLoadingMagicLink: false
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
            ChallengeWidgetContentStateView(
                stateKs: .completed(
                    .init(
                        headerData: headerData,
                        collectRewardButtonState: ChallengeWidgetViewStateContentCollectRewardButtonStateHidden(),
                        isLoadingMagicLink: false
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}

#Preview("Partially Completed") {
    let headerData = ChallengeWidgetViewStateContentHeaderData(
        title: "Advent Streak Challenge",
        description: "Close to victory, bonus bounty!",
        formattedDurationOfTime: "6 Oct - 12 Oct"
    )
    return ScrollView {
        VStack {
            ChallengeWidgetContentStateView(
                stateKs: .partiallyCompleted(
                    .init(
                        headerData: headerData,
                        collectRewardButtonState: ChallengeWidgetViewStateContentCollectRewardButtonStateVisible(
                            title: "Collect Reward"
                        ),
                        isLoadingMagicLink: false
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
            ChallengeWidgetContentStateView(
                stateKs: .partiallyCompleted(
                    .init(
                        headerData: headerData,
                        collectRewardButtonState: ChallengeWidgetViewStateContentCollectRewardButtonStateHidden(),
                        isLoadingMagicLink: false
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}

#Preview("Ended") {
    ScrollView {
        VStack {
            ChallengeWidgetContentStateView(
                stateKs: .ended(
                    .init(
                        headerData: ChallengeWidgetViewStateContentHeaderData(
                            title: "Advent Streak Challenge",
                            description: "Give it another shot next time!",
                            formattedDurationOfTime: "6 Oct - 12 Oct"
                        )
                    )
                ),
                onOpenDescriptionLink: { _ in },
                onDeadlineReloadTap: {},
                onCollectRewardTap: {}
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}

extension ChallengeWidgetViewStateContentHappeningNow.ProgressStatus {
    static var placeholder: [ChallengeWidgetViewStateContentHappeningNow.ProgressStatus] {
        [
            .completed, .missed, .active, .inactive, .inactive, .inactive, .inactive,
            .inactive, .inactive, .inactive, .inactive, .inactive, .inactive, .inactive,
            .inactive, .inactive, .inactive, .inactive, .inactive, .inactive, .inactive,
            .inactive, .inactive, .inactive
        ]
    }
}
#endif
