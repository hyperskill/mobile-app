import SwiftUI

extension StepQuizCodeView {
    struct Appearance {
        let codeEditorInsets = LayoutInsets(vertical: LayoutInsets.defaultInset)
        let codeEditorHeight: CGFloat = 128
    }
}

struct StepQuizCodeView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizCodeViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            let viewData = viewModel.viewData

            StepQuizCodeDetailsView(
                samples: viewData.samples,
                executionTimeLimit: viewData.executionTimeLimit,
                executionMemoryLimit: viewData.executionMemoryLimit
            )
            .padding(.horizontal, -LayoutInsets.defaultInset)

            StepQuizNameView(text: Strings.StepQuizCode.title, dividerLocation: .bottom)

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
                output: viewModel
            )
            .makeModule()
        }
    }
}

struct StepQuizCodeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizCodeAssembly
                .makePlaceholder()
                .makeModule()

            StepQuizCodeAssembly
                .makePlaceholder()
                .makeModule()
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
