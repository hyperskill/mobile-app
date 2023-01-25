import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    @StateObject var modalRouter: SwiftUIModalRouter

    @StateObject var panModalPresenter: PanModalPresenter

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
        }
        .navigationBarHidden(false)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
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
            StepTheoryContentView(
                viewData: viewModel.makeViewData(data.step),
                isPracticingAvailable: data.isPracticingAvailable,
                isPracticingLoading: data.stepCompletionState.isPracticingLoading,
                onStartPracticingTap: {
                    viewModel.doStartPracticing(currentStep: data.step)
                }
            )
        case Step.Type_.practice:
            StepQuizAssembly(
                step: data.step,
                stepRoute: viewModel.stepRoute,
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
            switch StepCompletionFeatureActionViewActionKs(stepCompletionViewAction.viewAction) {
            case .showPracticingErrorStatus(let showPracticingErrorStatusViewAction):
                ProgressHUD.showError(status: showPracticingErrorStatusViewAction.errorMessage)
            case .reloadStep(let reloadStepViewAction):
                stackRouter.replaceViewController(
                    StepAssembly(
                        stepRoute: reloadStepViewAction.stepRoute,
                        stackRouter: stackRouter
                    ).makeModule(),
                    animated: false
                )
            case .showTopicCompletedModal(let topicCompletedModalViewAction):
                presentTopicCompletedModal(modalText: topicCompletedModalViewAction.modalText)
            case .navigateTo(let navigateToViewAction):
                switch StepCompletionFeatureActionViewActionNavigateToKs(navigateToViewAction) {
                case .back:
                    panModalPresenter.dismissPanModal()
                    presentationMode.wrappedValue.dismiss()
                case .homeScreen:
                    panModalPresenter.dismissPanModal()
                    presentationMode.wrappedValue.dismiss()
                    TabBarRouter(tab: .home).route()
                }
            }
        }
    }
}

// MARK: - StepView (Modals) -

extension StepView {
    private func presentTopicCompletedModal(modalText: String) {
        viewModel.logTopicCompletedModalShownEvent()

        let panModal = TopicCompletedModalViewController(
            modalText: modalText,
            onGoToHomescreenButtonTap: viewModel.doGoToHomeScreenAction
        )

        panModal.onDisappear = viewModel.logTopicCompletedModalHiddenEvent

        panModalPresenter.presentPanModal(panModal)
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StepAssembly(stepRoute: StepRouteLearn(stepId: 4350), stackRouter: SwiftUIStackRouter()).makeModule()
        }
    }
}
