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
            viewModel.logViewedEvent()
        }
        .onDisappear(perform: viewModel.stopListening)
        .environmentObject(pushRouter)
        .environmentObject(modalRouter)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is StepFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadStep()
                }
        case is StepFeatureStateLoading:
            ProgressView()
        case is StepFeatureStateError:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: {
                        viewModel.loadStep(forceUpdate: true)
                    }
                )
            )
        case let data as StepFeatureStateData:
            buildContent(data: data)
                .navigationTitle(data.step.title)
        default:
            Text("Unkwown state")
        }
    }

    @ViewBuilder
    private func buildContent(data: StepFeatureStateData) -> some View {
        switch data.step.type {
        case Step.Type_.theory:
            StepTheoryContentView(
                viewData: viewModel.makeViewData(data.step)
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
