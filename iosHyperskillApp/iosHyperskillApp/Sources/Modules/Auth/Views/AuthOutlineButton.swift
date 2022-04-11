import SwiftUI

struct AuthOutlineButton: View {
    let text: String

    private let action: () -> Void

    init(text: String, action: @escaping () -> Void) {
        self.text = text
        self.action = action
    }

    var body: some View {
        Button(
            action: self.action,
            label: {
                Text(text)
                    .font(.body)
                    .frame(maxWidth: .infinity, minHeight: 48, alignment: .center)
                    .foregroundColor(Color(ColorPalette.primary))
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(Color(ColorPalette.primary.withAlphaComponent(0.38)), lineWidth: 1)
                    )
            }
        )
    }
}

struct AuthOutlineButton_Previews: PreviewProvider {
    static var previews: some View {
        AuthOutlineButton(text: "Continue with email", action: {})
            .padding()
    }
}
