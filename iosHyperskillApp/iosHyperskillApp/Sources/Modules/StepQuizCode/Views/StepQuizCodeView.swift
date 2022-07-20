import SwiftUI

struct StepQuizCodeView: View {
    @StateObject var viewModel: StepQuizCodeViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            StepQuizCodeDetailsView(
                samples: viewModel.viewData.samples,
                executionTimeLimit: viewModel.viewData.executionTimeLimit,
                executionMemoryLimit: viewModel.viewData.executionMemoryLimit
            )
            .padding(.horizontal, -LayoutInsets.defaultInset)

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                Text(Strings.StepQuizCode.title)
                    .font(.caption)
                    .foregroundColor(.disabledText)
                Divider()
            }
        }
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
