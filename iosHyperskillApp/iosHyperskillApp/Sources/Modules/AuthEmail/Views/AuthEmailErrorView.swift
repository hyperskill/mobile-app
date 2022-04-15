import SwiftUI

struct AuthEmailErrorView: View {
    var body: some View {
        Text(Strings.emailLoginErrorText)
            .foregroundColor(Color(ColorPalette.error))
            .font(.caption)
            .padding()
            .background(Color(ColorPalette.error).opacity(0.12))
            .cornerRadius(8)
    }
}

struct AuthEmailErrorView_Previews: PreviewProvider {
    static var previews: some View {
        AuthEmailErrorView()
    }
}
