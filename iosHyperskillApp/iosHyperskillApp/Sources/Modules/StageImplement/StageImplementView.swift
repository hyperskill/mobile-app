import shared
import SwiftUI

struct StageImplementView: View {
    @StateObject var viewModel: StageImplementViewModel

    @ObservedObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
        }
        .navigationBarHidden(false)
        .navigationBarTitleDisplayMode(.inline)
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
        case .idle, .loading:
            ProgressView()
        case .networkError:
            PlaceholderView(configuration: .networkError(action: viewModel.doRetryLoadStageImplement))
        case .content(let data):
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Text(data.stageTitle)
                        .font(.headline)
                        .foregroundColor(.primaryText)
                        .padding([.horizontal, .top])

                    StepAssembly.stageImplement(
                        stepRoute: data.stepRoute,
                        rootViewController: stackRouter.rootViewController
                    )
                    .makeModule()
                }
                .introspectScrollView { scrollView in
                    scrollView.shouldIgnoreScrollingAdjustment = true
                }
            }
            .navigationTitle(data.navigationTitle)
        }
    }
}

// MARK: - StageImplementView (ViewAction) -

private extension StageImplementView {
    func handleViewAction(_ viewAction: StageImplementFeatureActionViewAction) {
        switch StageImplementFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        case .showStageCompletedModal(let showStageCompletedModalViewAction):
            handleShowStageCompletedModalViewAction(showStageCompletedModalViewAction)
        case .showProjectCompletedModal(let showProjectCompletedModalViewAction):
            handleShowProjectCompletedModalViewAction(showProjectCompletedModalViewAction)
        }
    }

    func handleNavigateToViewAction(_ viewAction: StageImplementFeatureActionViewActionNavigateTo) {
        switch StageImplementFeatureActionViewActionNavigateToKs(viewAction) {
        case .studyPlan:
            TabBarRouter(
                tab: .studyPlan,
                popToRoot: true
            )
            .route()
        }
    }

    func handleShowStageCompletedModalViewAction(
        _ viewAction: StageImplementFeatureActionViewActionShowStageCompletedModal
    ) {
        let viewController = StageImplementStageCompletedModalViewController(
            title: viewAction.title,
            award: Int(viewAction.stageAward),
            delegate: viewModel
        )
        stackRouter.rootViewController?.presentIfPanModalWithCustomModalPresentationStyle(viewController)
    }

    func handleShowProjectCompletedModalViewAction(
        _ viewAction: StageImplementFeatureActionViewActionShowProjectCompletedModal
    ) {
        let viewController = StageImplementProjectCompletedModalViewController(
            stageAward: Int(viewAction.stageAward),
            projectAward: Int(viewAction.projectAward),
            delegate: viewModel
        )
        stackRouter.rootViewController?.presentIfPanModalWithCustomModalPresentationStyle(viewController)
    }
}

// MARK: - StageImplementView_Previews: PreviewProvider -

struct StageImplementView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StageImplementAssembly(
                projectID: 71,
                stageID: 390
            ).makeModule()
        }
    }
}
