import SwiftUI

struct ProgressScreenCallToActionButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(Color(ColorPalette.primary))
            .font(.subheadline)
            .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenCallToActionButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(ProgressScreenCallToActionButtonStyle())
            .padding()
    }
}
