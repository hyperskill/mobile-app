import SwiftUI

struct DebugStageImplementNavigationView: View {
    @Binding var projectIdText: String
    @Binding var stageIdText: String

    var isOpenButtonEnabled: Bool
    var onOpenButtonTapped: () -> Void

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TextField(
                Strings.DebugMenu.StageImplement.projectIdTextFieldTitle,
                text: $projectIdText
            )
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
            .frame(minHeight: 44)

            TextField(
                Strings.DebugMenu.StageImplement.stageIdTextFieldTitle,
                text: $stageIdText
            )
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
            .frame(minHeight: 44)

            Button(
                Strings.DebugMenu.StageImplement.buttonTitle,
                action: onOpenButtonTapped
            )
            .disabled(!isOpenButtonEnabled)
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }
}

struct DebugStageImplementNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        DebugStageImplementNavigationView(
            projectIdText: .constant(""),
            stageIdText: .constant(""),
            isOpenButtonEnabled: false,
            onOpenButtonTapped: {}
        )
        .padding()
    }
}
