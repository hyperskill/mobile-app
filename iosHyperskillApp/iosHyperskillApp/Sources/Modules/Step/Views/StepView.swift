import shared
import SwiftUI

struct StepView: View {
    @ObservedObject private var viewModel: StepViewModel

    init(viewModel: StepViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            switch self.viewModel.state {
            case is StepFeatureStateIdle:
                ProgressView().onAppear(perform: viewModel.loadStep)
            case is StepFeatureStateLoading:
                ProgressView()
            case is StepFeatureStateError:
                Text("Error")
            case let data as StepFeatureStateData:
                StepContentView(viewData: data.step.viewData)
                    .navigationTitle(data.step.title)
            default:
                Text("Unkwown state")
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        print("StepView :: \(#function) viewAction = \(viewAction)")
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        StepAssembly(stepID: 4350).makeModule()
    }
}
