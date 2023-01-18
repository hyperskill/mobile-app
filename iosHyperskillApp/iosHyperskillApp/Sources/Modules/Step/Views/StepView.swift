import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @StateObject var pushRouter: SwiftUIPushRouter

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
        .environmentObject(pushRouter)
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
            StepQuizAssembly(step: data.step, stepRoute: data.stepRoute)
                .makeModule()
                .environmentObject(PanModalPresenter())
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        switch StepFeatureActionViewActionKs(viewAction) {
        case .showStartPracticingErrorStatus:
            ProgressHUD.showError(status: Strings.Step.failedToStartPracticing)
        }
        print("StepView :: \(#function) viewAction = \(viewAction)")
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StepAssembly(stepRoute: StepRouteLearn(stepId: 4350)).makeModule()
        }
    }
}
