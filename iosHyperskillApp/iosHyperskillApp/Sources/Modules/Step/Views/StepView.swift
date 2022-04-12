import shared
import SwiftUI

struct StepView: View {
    @ObservedObject private var viewModel: StepViewModel

    init(viewModel: StepViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        Text("Step")
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        print("StepView :: \(#function) viewAction = \(viewAction)")
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        StepAssembly().makeModule()
    }
}
