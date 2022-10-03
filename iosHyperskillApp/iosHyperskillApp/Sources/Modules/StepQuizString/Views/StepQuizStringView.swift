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
    }
}

struct StepQuizStringView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizStringViewModel

    var body: some View {
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
            .addBorder()
    }
}

#if DEBUG
struct StepQuizStringView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizStringAssembly
            .makePlaceholder(dataType: .string)
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()

        StepQuizStringAssembly
            .makePlaceholder(dataType: .string)
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()
            .preferredColorScheme(.dark)
    }
}
#endif
