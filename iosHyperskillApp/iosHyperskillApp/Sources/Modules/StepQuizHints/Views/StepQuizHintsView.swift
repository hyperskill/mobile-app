import shared
import SVProgressHUD
import SwiftUI

extension StepQuizHintsView {
    struct Appearance {
        var skeletonInitialHeight: CGFloat = 34
        var skeletonHintHeight: CGFloat = 152
    }
}

struct StepQuizHintsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizHintsViewModel

    var body: some View {
        buildBody()
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)
            }
            .onDisappear {
                viewModel.stopListening()
                viewModel.onViewAction = nil
            }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is StepQuizHintsFeatureStateIdle:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonInitialHeight)
                .onAppear {
                    viewModel.loadHintsIDs()
                }
        case is StepQuizHintsFeatureStateLoading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHintHeight)
        case is StepQuizHintsFeatureStateNetworkError:
            StepQuizShowHintButton(
                text: Strings.Placeholder.networkErrorButtonText,
                showLightning: false
            ) {
                viewModel.onLoadHintButtonTap()
            }
        case let content as StepQuizHintsFeatureStateContent:
            buildContent(state: content)
        default:
            Text("Unkwown state")
        }
    }

    @ViewBuilder
    private func buildContent(state: StepQuizHintsFeatureStateContent) -> some View {
        if let hint = state.currentHint {
            StepQuizHintCardView(
                authorAvatarSource: hint.user.avatar,
                authorName: hint.user.fullName,
                hintText: hint.localizedText,
                hintHasReaction: state.hintHasReaction,
                onReactionTapped: viewModel.onHintReactionButtonTap(reaction:),
                onReportTapped: viewModel.logClickedReportEvent,
                onReportAlertAppeared: viewModel.logHintNoticeShownEvent,
                onReportAlertConfirmed: {
                    viewModel.onHintReportConfirmationButtonTap()
                    viewModel.logHintNoticeHiddenEvent(isReported: true)
                },
                onReportAlertCanceled: { viewModel.logHintNoticeHiddenEvent(isReported: false) },
                hasNextHints: !state.hintsIds.isEmpty,
                onNextHintTapped: viewModel.onLoadHintButtonTap
            )
        } else if !state.hintsIds.isEmpty {
            StepQuizShowHintButton(text: Strings.StepQuiz.Hints.showButton) {
                viewModel.onLoadHintButtonTap()
            }
        }
    }

    private func handleViewAction(_ viewAction: StepQuizHintsFeatureActionViewAction) {
        switch viewAction {
        case is StepQuizHintsFeatureActionViewActionShowNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        default:
            print("StepQuizHintsViewView :: \(#function) viewAction = \(viewAction)")
        }
    }
}

struct StepQuizHintsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizHintsAssembly(stepID: 15343).makeModule()
    }
}
