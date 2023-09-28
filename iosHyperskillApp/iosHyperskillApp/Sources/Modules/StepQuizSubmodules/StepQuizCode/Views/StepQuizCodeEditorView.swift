import SwiftUI

extension StepQuizCodeEditorView {
    struct Appearance {
        static let codeEditorMinHeight: CGFloat = 80

        let codeEditorInsets = LayoutInsets(vertical: LayoutInsets.defaultInset)
    }
}

struct StepQuizCodeEditorView: View {
    private(set) var appearance = Appearance()

    @Binding var code: String?
    let codeTemplate: String?

    let language: CodeLanguage?

    let onExpandButtonTap: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    @State private var height: CGFloat = Self.Appearance.codeEditorMinHeight

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
                        Image(.stepQuizCodeEditorExpand)
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
                textInsets: appearance.codeEditorInsets.uiEdgeInsets,
                onDidChangeHeight: { newHeight in
                    let constrainMinimumHeight = max(newHeight, Self.Appearance.codeEditorMinHeight)
                    guard constrainMinimumHeight != height else {
                        return
                    }

                    DispatchQueue.main.async {
                        height = constrainMinimumHeight
                        KeyboardManager.reloadLayoutIfNeeded()
                    }
                }
            )
            .frame(height: height)
            .frame(maxWidth: .infinity)

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
