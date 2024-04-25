import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @ObservedObject var stackRouter: SwiftUIStackRouter
    @ObservedObject var modalRouter: SwiftUIModalRouter
    @ObservedObject var panModalPresenter: PanModalPresenter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: viewModel.doScreenShowedAction,
                onViewDidDisappear: viewModel.doScreenHiddenAction
            )

            buildBody()
                .animation(.default, value: viewModel.state)

            let _ = renderStepToolbarViewState(viewModel.stepToolbarViewStateKs)
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
        switch viewModel.stepStateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    presentationMode: viewModel.isStageImplement ? .local : .fullscreen,
                    backgroundColor: .clear,
                    action: viewModel.doRetryLoadStep
                )
            )
        case .data(let data):
            buildContent(data: data)
        }
    }

    @ViewBuilder
    private func buildContent(data: StepFeatureStepStateData) -> some View {
        switch data.step.type.wrapped {
        case .theory:
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
                startPracticingButton: startPracticingButton,
                onTheoryFeedbackButtonTap: {
                    let assembly = StepTheoryFeedbackModalAssembly(stepRoute: viewModel.stepRoute)
                    modalRouter.present(module: assembly.makeModule(), modalPresentationStyle: .automatic)
                }
            )
            .navigationTitle(data.step.title)
        case .practice:
            StepQuizAssembly(
                step: data.step,
                stepRoute: viewModel.stepRoute,
                provideModuleInputCallback: { viewModel.stepQuizModuleInput = $0 },
                moduleOutput: viewModel
            )
            .makeModule()
            .environmentObject(panModalPresenter)
        }
    }

    private func renderStepToolbarViewState(_ stepToolbarViewState: StepToolbarFeatureViewStateKs) {
        switch stepToolbarViewState {
        case .idle, .loading:
            break
        case .unavailable, .error:
            stackRouter.rootViewController?.styledNavigationController?.hideProgress()
        case .content(let data):
            stackRouter.rootViewController?.styledNavigationController?.setProgress(data.progress, animated: true)
        }
    }

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        switch StepFeatureActionViewActionKs(viewAction) {
        case .stepCompletionViewAction(let stepCompletionViewAction):
            handleStepCompletionViewAction(stepCompletionViewAction.viewAction)
        case .stepToolbarViewAction:
            break
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
        case .navigateTo(let navigateToViewAction):
            switch StepCompletionFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .back:
                dismissPanModalAndNavigateBack()
            case .studyPlan:
                dismissPanModalAndNavigateBack()
                TabBarRouter(tab: .studyPlan).route()
            }
        case .showProblemOfDaySolvedModal(let showProblemOfDaySolvedModalViewAction):
            presentDailyStepCompletedModal(
                earnedGemsText: showProblemOfDaySolvedModalViewAction.earnedGemsText,
                shareStreakData: showProblemOfDaySolvedModalViewAction.shareStreakData
            )
        case .showShareStreakModal(let showShareStreakModalViewAction):
            presentShareStreakModal(streak: Int(showShareStreakModalViewAction.streak))
        case .showShareStreakSystemModal(let showShareStreakSystemModalViewAction):
            presentShareStreakSystemModal(streak: Int(showShareStreakSystemModalViewAction.streak))
        case .showRequestUserReviewModal(let showRequestUserReviewModalViewAction):
            presentRequestReviewModal(stepRoute: showRequestUserReviewModalViewAction.stepRoute)
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

// MARK: - StepView (Modals) -

private extension StepView {
    func presentTopicCompletedModal(modalText: String, isNextStepAvailable: Bool) {
        let modal = TopicCompletedModalViewController(
            modalText: modalText,
            isNextStepAvailable: isNextStepAvailable,
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(modal)
    }

    func presentDailyStepCompletedModal(
        earnedGemsText: String?,
        shareStreakData: StepCompletionFeatureShareStreakData
    ) {
        let modal = ProblemOfDaySolvedModalViewController(
            earnedGemsText: earnedGemsText,
            shareStreakData: StepCompletionFeatureShareStreakDataKs(shareStreakData),
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(modal)
    }

    func presentShareStreakModal(streak: Int) {
        let modal = ShareStreakModalViewController(
            streak: streak,
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(modal)
    }

    func presentShareStreakSystemModal(streak: Int) {
        let activityViewController = ShareStreakAction.makeActivityViewController(for: streak)
        modalRouter.present(module: activityViewController, modalPresentationStyle: .automatic)
    }

    func presentRequestReviewModal(stepRoute: StepRoute) {
        let assembly = RequestReviewModalAssembly(stepRoute: stepRoute)
        panModalPresenter.presentIfPanModal(assembly.makeModule())
    }
}

// MARK: - StepView_Previews: PreviewProvider -

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StepAssembly(stepRoute: StepRouteLearnStep(stepId: 4350, topicId: nil)).makeModule()
        }
    }
}
