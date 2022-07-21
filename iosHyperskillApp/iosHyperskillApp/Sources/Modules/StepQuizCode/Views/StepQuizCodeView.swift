import SwiftUI

extension StepQuizCodeView {
    struct Appearance {
        let codeEditorInsets = LayoutInsets(horizontal: 0, vertical: LayoutInsets.defaultInset)
        let codeEditorHeight: CGFloat = 128
    }
}

struct StepQuizCodeView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizCodeViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            StepQuizCodeDetailsView(
                samples: viewModel.viewData.samples,
                executionTimeLimit: viewModel.viewData.executionTimeLimit,
                executionMemoryLimit: viewModel.viewData.executionMemoryLimit
            )
            .padding(.horizontal, -LayoutInsets.defaultInset)

            StepQuizNameView(text: Strings.StepQuizCode.title, dividerLocation: .bottom)

            CodeEditor(
                code: .constant(viewModel.viewData.code),
                codeTemplate: viewModel.viewData.codeTemplate,
                language: viewModel.viewData.language,
                isEditable: false,
                textInsets: appearance.codeEditorInsets.uiEdgeInsets
            )
            .frame(height: appearance.codeEditorHeight)
            .frame(maxWidth: .infinity)
            .addBorder()
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
