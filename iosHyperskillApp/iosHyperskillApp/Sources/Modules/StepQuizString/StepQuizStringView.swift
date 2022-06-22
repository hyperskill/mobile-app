import SwiftUI

extension StepQuizStringView {
    struct Appearance {
        let textEditorTextColor = Color.primaryText
        let textEditorTextFont = Font.body
        let textEditorBorderColor = Color(ColorPalette.onSurfaceAlpha12)
        let textEditorHeight: CGFloat = 96
        let textEditorInsets = LayoutInsets.small

        let placeholderTextFont = Font.body
        let placeholderTextColor = Color.secondaryText
        let placeholderInsets = LayoutInsets(top: 16, left: 13, bottom: 16, right: 16)
    }
}

struct StepQuizStringView: View {
    private(set) var appearance = Appearance()

    @State private var text = ""

    var body: some View {
        TextEditor(text: $text)
            .foregroundColor(appearance.textEditorTextColor)
            .font(appearance.textEditorTextFont)
            .multilineTextAlignment(.leading)
            .disableAutocorrection(true)
            .autocapitalization(.none)
            .keyboardType(.default)
            .frame(height: appearance.textEditorHeight)
            .frame(maxWidth: .infinity)
            .padding(appearance.textEditorInsets.edgeInsets)
            .overlay(
                Text("Type your answer here...")
                    .font(appearance.placeholderTextFont)
                    .foregroundColor(appearance.placeholderTextColor)
                    .allowsHitTesting(false)
                    .padding(appearance.placeholderInsets.edgeInsets)
                    .opacity(text.isEmpty ? 1 : 0)
                ,
                alignment: .topLeading
            )
            .addBorder(color: appearance.textEditorBorderColor)
            .padding()
    }
}

struct StepQuizStringView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizStringView()
    }
}
