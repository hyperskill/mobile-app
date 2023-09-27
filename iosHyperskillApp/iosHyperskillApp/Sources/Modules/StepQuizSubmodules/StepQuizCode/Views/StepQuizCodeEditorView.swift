import SwiftUI

extension StepQuizCodeEditorView {
    struct Appearance {
        let codeEditorInsets = LayoutInsets(vertical: LayoutInsets.defaultInset)
        let codeEditorMinHeightHeight: CGFloat = 300
    }
}

struct StepQuizCodeEditorView: View {
    private(set) var appearance = Appearance()

    @Binding var code: String?
    let codeTemplate: String?

    let language: CodeLanguage?

    let onExpandButtonTap: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: 0) {
            Divider()

            HStack {
                Text(Strings.StepQuizCode.codeEditorTitle)
                    .font(.headline)
                    .foregroundColor(.primaryText)

                if let languageName = language?.humanReadableName {
                    Text("(\(languageName))")
                        .font(.subheadline)
                        .foregroundColor(.tertiaryText)
                }

                Spacer()

                Button(
                    action: onExpandButtonTap,
                    label: {
                        Image(Images.StepQuiz.expand)
                            .renderingMode(.template)
                            .font(.headline)
                            .foregroundColor(.secondaryText)
                            .aspectRatio(contentMode: .fit)
                            .opacity(isEnabled ? 1 : 0.38)
                    }
                )
            }
            .padding(.horizontal)
            .padding(.vertical, LayoutInsets.smallInset)
            .background(BackgroundView())

            Divider()

            CodeEditor(
                code: $code,
                codeTemplate: codeTemplate,
                language: language,
                isEditable: true,
                textInsets: appearance.codeEditorInsets.uiEdgeInsets
            )
            .frame(
                maxWidth: .infinity,
                minHeight: appearance.codeEditorMinHeightHeight
            )

            Divider()
        }
    }
}

struct StepQuizCodeEditorView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeEditorView(
            code: .constant(CodeLanguageSamples.sample(for: .java)),
            codeTemplate: nil,
            language: .java,
            onExpandButtonTap: {}
        )
    }
}
