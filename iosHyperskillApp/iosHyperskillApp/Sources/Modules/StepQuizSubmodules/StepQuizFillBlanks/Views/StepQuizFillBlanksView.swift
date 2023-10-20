import SwiftUI

struct StepQuizFillBlanksView: View {
    @StateObject var viewModel: StepQuizFillBlanksViewModel

    var body: some View {
        FillBlanksQuizViewWrapper(
            viewData: viewModel.viewData
        )
        .onAppear(perform: viewModel.doProvideModuleInput)
    }
}

//#Preview {
//    StepQuizFillBlanksView()
//}
