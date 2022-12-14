import SwiftUI

extension StepQuizSQLView {
    struct Appearance {
        let codeEditorInsets = LayoutInsets(vertical: LayoutInsets.defaultInset)
        let codeEditorHeight: CGFloat = 128
    }
}

struct StepQuizSQLView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizSQLViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            let viewData = viewModel.viewData

            CodeEditor(
                code: .constant(viewData.code),
                codeTemplate: viewData.codeTemplate,
                language: viewData.language,
                isEditable: false,
                textInsets: appearance.codeEditorInsets.uiEdgeInsets
            )
            .frame(height: appearance.codeEditorHeight)
            .frame(maxWidth: .infinity)
            .addBorder()
            .onTapGesture {
                if isEnabled {
                    viewModel.navigationState.presentingFullScreen = true
                }
            }
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
struct StepQuizSQLView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizSQLAssembly
                .makePlaceholder()
                .makeModule()

            StepQuizSQLAssembly
                .makePlaceholder()
                .makeModule()
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
#endif
