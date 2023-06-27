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
        switch viewModel.stateKs {
        case .idle:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonInitialHeight)
        case .initialLoading, .hintLoading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHintHeight)
        case .error:
            StepQuizShowHintButton(
                text: Strings.Placeholder.networkErrorButtonText,
                showLightning: false,
                onClick: viewModel.onLoadHintButtonTap
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
                onClick: viewModel.onLoadHintButtonTap
            )
        case .hintCard(let data):
            StepQuizHintCardView(
                authorAvatarSource: data.authorAvatar,
                authorName: data.authorName,
                hintText: data.hintText,
                hintState: data.hintState,
                onReactionTapped: viewModel.onHintReactionButtonTap(reaction:),
                onReportTapped: viewModel.logClickedReportEvent,
                onReportAlertAppeared: viewModel.logHintNoticeShownEvent,
                onReportAlertConfirmed: {
                    viewModel.onHintReportConfirmationButtonTap()
                    viewModel.logHintNoticeHiddenEvent(isReported: true)
                },
                onReportAlertCanceled: { viewModel.logHintNoticeHiddenEvent(isReported: false) },
                onNextHintTapped: viewModel.onLoadHintButtonTap
            )
        }
    }

    private func handleViewAction(_ viewAction: StepQuizHintsFeatureActionViewAction) {
        switch StepQuizHintsFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        }
    }
}

struct StepQuizHintsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizHintsAssembly(stepID: 15343, stepRoute: StepRouteLearnStep(stepId: 15343)).makeModule()
    }
}
