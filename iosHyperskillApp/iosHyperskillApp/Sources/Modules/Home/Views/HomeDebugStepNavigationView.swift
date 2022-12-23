import SwiftUI

struct HomeDebugStepNavigationView: View {
    @State private var stepInput = ""

    var onOpenStepTapped: ((Int) -> Void)?

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TextField(
                "Enter step id",
                text: $stepInput
            )
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
            .frame(minHeight: 44)

            Button("Open step") {
                guard let stepID = Int(stepInput) else {
                    return
                }

                onOpenStepTapped?(stepID)
            }
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
        .addBorder()
    }
}

struct HomeDebugStepNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        HomeDebugStepNavigationView()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
