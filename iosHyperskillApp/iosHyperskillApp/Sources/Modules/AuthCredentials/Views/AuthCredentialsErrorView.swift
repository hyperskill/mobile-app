import SwiftUI

struct AuthCredentialsErrorView: View {
    let message: String

    var body: some View {
        Text(message)
            .foregroundColor(Color(ColorPalette.error))
            .font(.caption)
            .frame(maxWidth: .infinity)
            .padding()
            .textSelectionCompatibility(fallbackStringToCopy: message)
            .background(Color(ColorPalette.error).opacity(0.12))
            .cornerRadius(8)
    }
}

#Preview {
    AuthCredentialsErrorView(
        message: "Error message"
    )
    .padding()
}
