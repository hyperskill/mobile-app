import SwiftUI

struct HomeDebugStepNavigationView: View {
    @State private var stepInput = ""

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TextField(
                "Enter step id",
                text: $stepInput
            )
            .keyboardType(.numberPad)
            .disableAutocorrection(true)
            .frame(minHeight: 44)

            NavigationLink("Open step") {
                if let stepID = Int(stepInput) {
                    StepAssembly(stepID: stepID)
                        .makeModule()
                }
            }
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
        .addBorder()
        .shadow(color: .black.opacity(0.05), radius: 8, x: 0, y: 2)
    }
}

struct HomeDebugStepNavigationView_Previews: PreviewProvider {
    static var previews: some View {
        HomeDebugStepNavigationView()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
