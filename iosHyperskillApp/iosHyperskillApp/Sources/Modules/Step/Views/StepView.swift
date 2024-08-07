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
                onViewDidAppear: {
                    viewModel.onViewAction = handleViewAction(_:)
                    viewModel.startListening()

                    viewModel.doLoadStep()
                    viewModel.doScreenShowedAction()
                },
                onViewWillDisappear: {
                    viewModel.onViewAction = nil
                    viewModel.stopListening()
                },
                onViewDidDisappear: viewModel.doScreenHiddenAction
            )

            buildBody()
                .animation(.default, value: viewModel.state)

            let _ = renderStepToolbarViewState(viewModel.stepToolbarViewStateKs)
            let _ = handleLoadingIndicatorVisibility(isVisible: viewModel.state.isLoadingShowed)
        }
        .navigationBarHidden(false)
        .navigationBarTitleDisplayMode(.inline)
        .stepToolbar(
            state: viewModel.state,
            onCommentButtonTap: viewModel.doCommentToolbarAction,
            onShareButtonTap: viewModel.doShareToolbarMenuAction,
            onReportButtonTap: viewModel.doReportToolbarMenuAction,
            onSkipButtonTap: viewModel.doSkipToolbarMenuAction,
            onOpenInWebButtonTap: viewModel.doOpenInWebToolbarMenuAction
        )
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
                startPracticingButton: startPracticingButton
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
            .environmentObject(stackRouter)
            .environmentObject(modalRouter)
            .environmentObject(panModalPresenter)
        }
    }

    private func renderStepToolbarViewState(_ stepToolbarViewState: StepToolbarFeatureViewStateKs) {
        guard let styledNavigationController = stackRouter.rootViewController?.styledNavigationController else {
            return
        }

        switch stepToolbarViewState {
        case .idle, .loading:
            break
        case .unavailable, .error:
            styledNavigationController.hideProgress()
        case .content(let data):
            styledNavigationController.setProgress(data.progress, animated: true)
            styledNavigationController.setOnSpacebotHeadTapAction(viewModel.logSpacebotClickedEvent)
        }
    }

    private func handleLoadingIndicatorVisibility(isVisible: Bool) {
        if isVisible {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismissWithDelay()
        }
    }
}

// MARK: - StepView (ViewAction) -

private extension StepView {
    func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        switch StepFeatureActionViewActionKs(viewAction) {
        case .stepCompletionViewAction(let stepCompletionViewAction):
            handleStepCompletionViewAction(stepCompletionViewAction.viewAction)
        case .stepToolbarViewAction:
            break
        case .openUrl(let openUrlViewAction):
            WebControllerManager.shared.presentWebControllerWithURLString(
                openUrlViewAction.url,
                controllerType: .inAppSafari
            )
        case .reloadStep(let reloadStepViewAction):
            reloadStep(stepRoute: reloadStepViewAction.stepRoute)
        case .shareStepLink(let shareStepLinkViewAction):
            guard let url = URL(string: shareStepLinkViewAction.link) else {
                return
            }

            let activityViewController = UIActivityViewController(activityItems: [url], applicationActivities: nil)
            if let popoverPresentationController = activityViewController.popoverPresentationController {
                popoverPresentationController.sourceView = modalRouter.rootViewController?.view
            }

            modalRouter.present(module: activityViewController, modalPresentationStyle: .fullScreen)
        case .showCantSkipError:
            ProgressHUD.showError(status: Strings.Step.stepSkipFailedMessage)
        case .showFeedbackModal(let showFeedbackModalViewAction):
            let assembly = StepFeedbackAssembly(stepRoute: showFeedbackModalViewAction.stepRoute)
            modalRouter.present(module: assembly.makeModule(), modalPresentationStyle: .automatic)
        case .showLoadingError:
            ProgressHUD.showError(status: Strings.Common.error)
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        }
    }

    func handleStepCompletionViewAction(_ viewAction: StepCompletionFeatureActionViewAction) {
        switch StepCompletionFeatureActionViewActionKs(viewAction) {
        case .showStartPracticingError(let showPracticingErrorStatusViewAction):
            ProgressHUD.showError(status: showPracticingErrorStatusViewAction.message)
        case .reloadStep(let reloadStepViewAction):
            reloadStep(stepRoute: reloadStepViewAction.stepRoute)
        case .showTopicCompletedModal(let topicCompletedModalViewAction):
            presentTopicCompletedModal(
                params: topicCompletedModalViewAction.params
            )
        case .navigateTo(let navigateToViewAction):
            switch StepCompletionFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .back:
                dismissPanModalAndNavigateBack()
            case .studyPlan:
                dismissPanModalAndNavigateBack()
                TabBarRouter(tab: .studyPlan).route()
            case .paywall(let navigateToPaywallViewAction):
                let assembly = PaywallAssembly(
                    source: navigateToPaywallViewAction.paywallTransitionSource
                )
                modalRouter.present(module: assembly.makeModule())
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
        case .hapticFeedback(let hapticFeedbackViewAction):
            switch StepCompletionFeatureActionViewActionHapticFeedbackKs(hapticFeedbackViewAction) {
            case .topicCompleted:
                FeedbackGenerator(feedbackType: .notification(.success)).triggerFeedback()
            }
        }
    }

    func dismissPanModalAndNavigateBack() {
        let isDismissed = panModalPresenter.dismissPanModal(
            animated: true,
            completion: stackRouter.popViewController
        )

        if !isDismissed {
            stackRouter.popViewController()
        }
    }

    func reloadStep(stepRoute: StepRoute) {
        stackRouter.replaceTopViewController(
            StepAssembly(stepRoute: stepRoute).makeModule(),
            animated: false
        )
    }

    func handleNavigateToViewAction(_ viewAction: StepFeatureActionViewActionNavigateTo) {
        switch StepFeatureActionViewActionNavigateToKs(viewAction) {
        case .commentsScreen(let data):
            let assembly = CommentsAssembly(params: data.params)
            let navigationController = UINavigationController(rootViewController: assembly.makeModule())
            modalRouter.present(module: navigationController, modalPresentationStyle: .automatic)
        }
    }
}

// MARK: - StepView (Modals) -

private extension StepView {
    func presentTopicCompletedModal(params: TopicCompletedModalFeatureParams) {
        let assembly = TopicCompletedModalAssembly(
            params: params,
            output: viewModel
        )
        modalRouter.present(module: assembly.makeModule())
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
