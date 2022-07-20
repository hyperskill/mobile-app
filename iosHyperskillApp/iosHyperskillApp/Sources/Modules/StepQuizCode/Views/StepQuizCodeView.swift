import SwiftUI

struct StepQuizCodeView: View {
    @StateObject var viewModel: StepQuizCodeViewModel

    var body: some View {
        StepQuizCodeDetailsView(samples: viewModel.viewData.samples)
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
