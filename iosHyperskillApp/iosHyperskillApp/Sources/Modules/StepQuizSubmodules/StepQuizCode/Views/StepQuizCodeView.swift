import SwiftUI

struct StepQuizCodeView: View {
    @StateObject var viewModel: StepQuizCodeViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            let viewData = viewModel.viewData

            StepQuizCodeDetailsView(
                samples: viewData.samples,
                onExpandTapped: viewModel.logClickedCodeDetailsEvent
            )

            StepQuizCodeEditorView(
                code: Binding(
                    get: { viewModel.viewData.code },
                    set: { viewModel.handleCodeDidChange($0) }
                ),
                codeTemplate: viewData.codeTemplate,
                language: viewData.language,
                onExpandButtonTap: viewModel.doFullScreenCodeEditorPresentation
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
#Preview("Light") {
    StepQuizCodeAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
}

#Preview("Dark") {
    StepQuizCodeAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
        .preferredColorScheme(.dark)
}
#endif
