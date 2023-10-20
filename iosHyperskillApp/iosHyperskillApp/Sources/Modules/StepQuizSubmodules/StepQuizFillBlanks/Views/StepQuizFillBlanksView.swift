import SwiftUI

struct StepQuizFillBlanksView: View {
    @StateObject var viewModel: StepQuizFillBlanksViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        FillBlanksQuizViewWrapper(
            components: viewModel.viewData.components,
            onInputDidChange: viewModel.doInputTextUpdate(_:for:)
        )
        .onAppear(perform: viewModel.doProvideModuleInput)
        .opacity(isEnabled ? 1 : 0.5)
    }
}

//#Preview {
//    StepQuizFillBlanksView()
//}
