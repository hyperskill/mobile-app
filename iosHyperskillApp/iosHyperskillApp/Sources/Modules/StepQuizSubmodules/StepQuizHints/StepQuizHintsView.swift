import shared
import SwiftUI

extension StepQuizHintsView {
    struct Appearance {
        let skeletonInitialSize = CGSize(width: 146, height: 34)
        let skeletonHintHeight: CGFloat = 152
    }
}

struct StepQuizHintsView: View {
    private(set) var appearance = Appearance()

    var state: StepQuizHintsFeatureState
    var onNewMessage: (StepQuizHintsFeatureMessage) -> Void

    private var viewStateKs: StepQuizHintsFeatureViewStateKs {
        let viewState = StepQuizHintsViewStateMapper.shared.mapState(state: state)
        return StepQuizHintsFeatureViewStateKs(viewState)
    }

    var body: some View {
        buildBody()
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewStateKs {
        case .idle:
            EmptyView()
        case .initialLoading:
            SkeletonRoundedView(appearance: .init(size: appearance.skeletonInitialSize))
        case .hintLoading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHintHeight)
        case .error:
            StepQuizShowHintButton(
                text: Strings.Placeholder.networkErrorButtonText,
                showLightning: false,
                onClick: {
                    onNewMessage(StepQuizHintsFeatureMessageLoadHintButtonClicked())
                }
            )
        case .content(let sealedState):
            buildContent(state: StepQuizHintsFeatureViewStateContentKs(sealedState))
        }
    }

    @ViewBuilder
    private func buildContent(state: StepQuizHintsFeatureViewStateContentKs) -> some View {
        switch state {
        case .seeHintButton:
            StepQuizShowHintButton(
                text: Strings.StepQuiz.Hints.showButton,
                onClick: {
                    onNewMessage(StepQuizHintsFeatureMessageLoadHintButtonClicked())
                }
            )
        case .hintCard(let data):
            StepQuizHintCardView(
                authorAvatarSource: data.authorAvatar,
                authorName: data.authorName,
                hintText: data.hintText,
                hintState: data.hintState,
                onReactionTapped: { reaction in
                    onNewMessage(StepQuizHintsFeatureMessageReactionButtonClicked(reaction: reaction))
                },
                onReportTapped: {
                    onNewMessage(StepQuizHintsFeatureMessageClickedReportEventMessage())
                },
                onReportAlertAppeared: {
                    onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeShownEventMessage())
                },
                onReportAlertConfirmed: {
                    onNewMessage(StepQuizHintsFeatureMessageReportHint())
                    onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeHiddenEventMessage(isReported: true))
                },
                onReportAlertCanceled: {
                    onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeHiddenEventMessage(isReported: true))
                },
                onNextHintTapped: {
                    onNewMessage(StepQuizHintsFeatureMessageLoadHintButtonClicked())
                }
            )
        }
    }
}

extension StepQuizHintsView: Equatable {
    static func == (lhs: StepQuizHintsView, rhs: StepQuizHintsView) -> Bool {
        StepQuizHintsFeatureStateKs(lhs.state) == StepQuizHintsFeatureStateKs(rhs.state)
    }
}

#Preview {
    StepQuizHintsView(
        state: StepQuizHintsFeatureStateIdle(),
        onNewMessage: { _ in }
    )
}

#Preview {
    StepQuizHintsView(
        state: StepQuizHintsFeatureStateLoading(isInitialLoading: true),
        onNewMessage: { _ in }
    )
}

#Preview {
    StepQuizHintsView(
        state: StepQuizHintsFeatureStateLoading(isInitialLoading: false),
        onNewMessage: { _ in }
    )
    .padding()
}
