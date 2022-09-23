import shared
import SwiftUI

struct StepView: View {
    @StateObject var viewModel: StepViewModel

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        buildBody()
            .navigationBarBackButtonTitleRemoved {
                viewModel.logClickedBackEvent()
                presentationMode.wrappedValue.dismiss()
            }
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)
            }
            .onDisappear(perform: viewModel.stopListening)
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
                configuration: .networkError {
                    viewModel.loadStep(forceUpdate: true)
                }
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
            StepQuizAssembly(step: data.step)
                .makeModule()
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
        StepAssembly(stepID: 4350).makeModule()
    }
}
