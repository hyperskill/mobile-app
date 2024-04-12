import SwiftUI

extension StepQuizStringView {
    struct Appearance {
        let textEditorTextColor = Color.primaryText
        let textEditorTextFont = Font.body
        let textEditorHeight: CGFloat = 96
        let textEditorInsets = LayoutInsets.small

        let placeholderTextFont = Font.body
        let placeholderTextColor = Color.secondaryText
        let placeholderInsets = LayoutInsets(top: 16, leading: 13, bottom: 16, trailing: 16)

        let borderColor = Color(UIColor.dynamic(light: ColorPalette.onSurfaceAlpha12, dark: .separator))
    }
}

struct StepQuizStringView: View {
    private(set) var appearance = Appearance()

    @ObservedObject var viewModel: StepQuizStringViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TextEditor(text: $viewModel.viewData.text)
                .foregroundColor(appearance.textEditorTextColor)
                .font(appearance.textEditorTextFont)
                .multilineTextAlignment(.leading)
                .disableAutocorrection(true)
                .autocapitalization(.none)
                .keyboardType(viewModel.viewData.isDecimalTextInput ? .decimalPad : .default)
                .frame(height: appearance.textEditorHeight)
                .frame(maxWidth: .infinity)
                .padding(appearance.textEditorInsets.edgeInsets)
                .overlay(
                    Text(viewModel.viewData.placeholder)
                        .font(appearance.placeholderTextFont)
                        .foregroundColor(appearance.placeholderTextColor)
                        .allowsHitTesting(false)
                        .padding(appearance.placeholderInsets.edgeInsets)
                        .opacity(viewModel.viewData.text.isEmpty ? 1 : 0)
                    ,
                    alignment: .topLeading
                )
                .addBorder(color: appearance.borderColor)

            if viewModel.viewData.isForceScoreCheckboxVisible {
                StepQuizStringForcePromptScoreView(
                    isSelected: viewModel.viewData.isForceScoreCheckboxChecked,
                    onTap: viewModel.doForceScoreCheckboxMainAction
                )
            }
        }
        .opacity(isEnabled ? 1 : 0.5)
        .animation(.easeInOut(duration: 0.33), value: isEnabled)
    }
}

#if DEBUG
#Preview("string") {
    StepQuizStringAssembly
        .makePlaceholder(dataType: .string)
        .makeModule()
        .padding()
}

#Preview("prompt") {
    StepQuizStringAssembly
        .makePlaceholder(dataType: .prompt)
        .makeModule()
        .padding()
}

#Preview("Disabled") {
    StepQuizStringAssembly
        .makePlaceholder(dataType: .prompt)
        .makeModule()
        .padding()
        .disabled(true)
}
#endif
