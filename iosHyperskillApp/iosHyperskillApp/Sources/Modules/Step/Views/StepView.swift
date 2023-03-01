import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @StateObject var stackRouter: SwiftUIStackRouter
    @StateObject var modalRouter: SwiftUIModalRouter
    @StateObject var panModalPresenter: PanModalPresenter

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
        .environmentObject(stackRouter)
        .environmentObject(modalRouter)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            ProgressView()
                .onAppear {
                    viewModel.loadStep()
                }
        case .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: {
                        viewModel.loadStep(forceUpdate: true)
                    }
                )
            )
        case .data(let data):
            buildContent(data: data)
                .navigationTitle(data.step.title)
        }
    }

    @ViewBuilder
    private func buildContent(data: StepFeatureStateData) -> some View {
        switch data.step.type {
        case Step.Type_.theory:
            let startPracticingButton: StepTheoryContentView.StartPracticingButton? = {
                guard data.isPracticingAvailable else {
                    return nil
                }

                return .init(
                    isLoading: data.stepCompletionState.isPracticingLoading,
                    action: viewModel.doStartPracticing
                )
            }()

            StepTheoryContentView(
                viewData: viewModel.makeViewData(data.step),
                startPracticingButton: startPracticingButton
            )
        case Step.Type_.practice:
            StepQuizAssembly(
                step: data.step,
                stepRoute: viewModel.stepRoute,
                provideModuleInputCallback: { viewModel.stepQuizModuleInput = $0 },
                moduleOutput: viewModel
            )
            .makeModule()
            .environmentObject(panModalPresenter)
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        switch StepFeatureActionViewActionKs(viewAction) {
        case .stepCompletionViewAction(let stepCompletionViewAction):
            handleStepCompletionViewAction(stepCompletionViewAction.viewAction)
        }
    }

    private func handleStepCompletionViewAction(_ viewAction: StepCompletionFeatureActionViewAction) {
        switch StepCompletionFeatureActionViewActionKs(viewAction) {
        case .showStartPracticingError(let showPracticingErrorStatusViewAction):
            ProgressHUD.showError(status: showPracticingErrorStatusViewAction.message)
        case .reloadStep(let reloadStepViewAction):
            stackRouter.replaceTopViewController(
                StepAssembly(stepRoute: reloadStepViewAction.stepRoute).makeModule(),
                animated: false
            )
        case .showTopicCompletedModal(let topicCompletedModalViewAction):
            presentTopicCompletedModal(
                modalText: topicCompletedModalViewAction.modalText,
                isNextStepAvailable: topicCompletedModalViewAction.isNextStepAvailable
            )
        case .requestDailyStudyRemindersPermission:
            presentSendDailyStudyRemindersPermissionAlert()
        case .showProblemOfDaySolvedModal(let showProblemOfDaySolvedModalViewAction):
            presentDailyStepCompletedModal(earnedGemsText: showProblemOfDaySolvedModalViewAction.earnedGemsText)
        case .navigateTo(let navigateToViewAction):
            switch StepCompletionFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .back:
                dismissPanModalAndNavigateBack()
            case .homeScreen:
                dismissPanModalAndNavigateBack()
                TabBarRouter(tab: .home).route()
            }
        }
    }

    private func dismissPanModalAndNavigateBack() {
        let isDismissed = panModalPresenter.dismissPanModal(
            animated: true,
            completion: stackRouter.popViewController
        )

        if !isDismissed {
            stackRouter.popViewController()
        }
    }
}

// MARK: - StepView (Alerts) -

extension StepView {
    private func presentSendDailyStudyRemindersPermissionAlert() {
        let alert = UIAlertController(
            title: Strings.Step.DailyStudyRemindersPermissionAlert.title,
            message: Strings.Step.DailyStudyRemindersPermissionAlert.message,
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.ok,
                style: .default,
                handler: { [weak viewModel] _ in
                    viewModel?.handleSendDailyStudyRemindersPermissionRequestResult(isGranted: true)
                }
            )
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.later,
                style: .cancel,
                handler: { [weak viewModel] _ in
                    viewModel?.handleSendDailyStudyRemindersPermissionRequestResult(isGranted: false)
                }
            )
        )

        modalRouter.presentAlert(alert)
    }
}

// MARK: - StepView (Modals) -

extension StepView {
    private func presentTopicCompletedModal(modalText: String, isNextStepAvailable: Bool) {
        let modal = TopicCompletedModalViewController(
            modalText: modalText,
            isNextStepAvailable: isNextStepAvailable,
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(modal)
    }

    private func presentDailyStepCompletedModal(earnedGemsText: String) {
        let panModal = ProblemOfDaySolvedModalViewController(
            earnedGemsText: earnedGemsText,
            delegate: viewModel
        )

        panModalPresenter.presentPanModal(panModal)
    }
}

// MARK: - StepView_Previews: PreviewProvider -

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StepAssembly(stepRoute: StepRouteLearn(stepId: 4350)).makeModule()
        }
    }
}
