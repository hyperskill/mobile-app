import SwiftUI

struct StepQuizSQLView: View {
    @StateObject var viewModel: StepQuizSQLViewModel

    @Environment(\.isEnabled) private var isEnabled
    @Environment(\.isFixCodeMistakesBadgeVisible) private var isFixCodeMistakesBadgeVisible

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            let viewData = viewModel.viewData

            if isFixCodeMistakesBadgeVisible {
                StepQuizCodeFixCodeMistakesBadge()
            }

            StepQuizCodeEditorView(
                code: Binding(
                    get: { viewModel.viewData.code },
                    set: { viewModel.handleCodeDidChange($0) }
                ),
                codeTemplate: viewData.codeTemplate,
                language: viewData.language,
                languageHumanReadableName: viewData.languageHumanReadableName,
                onExpandButtonTap: viewModel.doFullScreenCodeEditorPresentation,
                onInputAccessoryButtonTap: viewModel.logClickedInputAccessoryButton(symbol:)
            )
            .padding(.horizontal, -LayoutInsets.defaultInset)
        }
        .fullScreenCover(isPresented: $viewModel.navigationState.presentingFullScreen) {
            StepQuizCodeFullScreenAssembly(
                codeQuizViewData: viewModel.viewData,
                navigationTitle: Strings.StepQuizSQL.title,
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
    StepQuizSQLAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
}

#Preview("Dark") {
    StepQuizSQLAssembly
        .makePlaceholder()
        .makeModule()
        .preferredColorScheme(.dark)
        .padding()
}
#endif
