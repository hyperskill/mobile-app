import SwiftUI

struct StepQuizCodeView: View {
    @StateObject var viewModel: StepQuizCodeViewModel

    var body: some View {
        StepQuizCodeDetailsView(
            samples: viewModel.viewData.samples,
            executionTimeLimit: viewModel.viewData.executionTimeLimit,
            executionMemoryLimit: viewModel.viewData.executionMemoryLimit
        )
    }
}

struct StepQuizCodeView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeAssembly
            .makePlaceholder()
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
