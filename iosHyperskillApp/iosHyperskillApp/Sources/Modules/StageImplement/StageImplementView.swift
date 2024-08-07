import shared
import SwiftUI

struct StageImplementView: View {
    @StateObject var viewModel: StageImplementViewModel

    let stackRouter: StackRouter
    let panModalPresenter: PanModalPresenter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: {
                    viewModel.onViewAction = handleViewAction(_:)
                    viewModel.startListening()

                    viewModel.doLoadStageImplement()
                    viewModel.logViewedEvent()
                },
                onViewWillDisappear: {
                    viewModel.onViewAction = nil
                    viewModel.stopListening()
                }
            )

            buildBody()
        }
        .navigationBarHidden(false)
        .navigationBarTitleDisplayMode(.inline)
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
        panModalPresenter.presentPanModal(viewController)
    }

    func handleShowProjectCompletedModalViewAction(
        _ viewAction: StageImplementFeatureActionViewActionShowProjectCompletedModal
    ) {
        let viewController = StageImplementProjectCompletedModalViewController(
            stageAward: Int(viewAction.stageAward),
            projectAward: Int(viewAction.projectAward),
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(viewController)
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
