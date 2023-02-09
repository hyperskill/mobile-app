import SwiftUI

struct DebugStepNavigationView: View {
    @Binding var text: String

    var isOpenButtonEnabled: Bool
    var openButtonTapped: () -> Void

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TextField(
                Strings.DebugMenu.StepNavigation.textFieldTitle,
                text: $text
            )
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
            .frame(minHeight: 44)

            Button(
                Strings.DebugMenu.StepNavigation.buttonTitle,
                action: openButtonTapped
            )
            .disabled(!isOpenButtonEnabled)
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }
}

struct DebugStepNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        DebugStepNavigationView(
            text: .constant(""),
            isOpenButtonEnabled: true,
            openButtonTapped: {}
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
