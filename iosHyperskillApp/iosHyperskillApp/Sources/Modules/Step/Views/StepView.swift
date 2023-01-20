import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    @StateObject var modalRouter: SwiftUIModalRouter

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
                practiceStatus: data.practiceStatus,
                onStartPracticingTap: viewModel.doStartPracticing
            )
        case Step.Type_.practice:
            StepQuizAssembly(step: data.step, stepRoute: viewModel.stepRoute)
                .makeModule()
                .environmentObject(PanModalPresenter())
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        switch StepFeatureActionViewActionKs(viewAction) {
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
        }
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StepAssembly(stepRoute: StepRouteLearn(stepId: 4350), stackRouter: SwiftUIStackRouter()).makeModule()
        }
    }
}
