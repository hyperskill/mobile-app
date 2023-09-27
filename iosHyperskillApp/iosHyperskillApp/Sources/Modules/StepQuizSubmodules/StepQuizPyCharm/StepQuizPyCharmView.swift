import SwiftUI

struct StepQuizPyCharmView: View {
    @StateObject var viewModel: StepQuizPyCharmViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            let viewData = viewModel.viewData

            StepQuizCodeEditorView(
                code: Binding(
                    get: { viewModel.viewData.code },
                    set: { viewModel.handleCodeDidChange($0) }
                ),
                codeTemplate: viewData.codeTemplate,
                language: viewData.language,
                onExpandButtonTap: {
                    viewModel.navigationState.presentingFullScreen = true
                }
            )
            .padding(.horizontal, -LayoutInsets.defaultInset)
        }
        .fullScreenCover(isPresented: $viewModel.navigationState.presentingFullScreen) {
            StepQuizCodeFullScreenAssembly(
                codeQuizViewData: viewModel.viewData,
                provideModuleInputCallback: { viewModel.fullScreenModuleInput = $0 },
                output: viewModel
            )
            .makeModule()
        }
        .onAppear(perform: viewModel.doProvideModuleInput)
    }
}

#if DEBUG
struct StepQuizPyCharmView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizPyCharmAssembly
            .makePlaceholder()
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
#endif
