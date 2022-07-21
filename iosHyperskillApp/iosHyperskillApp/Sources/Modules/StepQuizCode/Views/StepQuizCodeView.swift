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

    @State private var isPresentedFullScreen = false

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
            .onTapGesture {
                isPresentedFullScreen = true
            }
        }
        .fullScreenCover(isPresented: $isPresentedFullScreen) {
            StepQuizCodeFullScreenAssembly().makeModule()
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
