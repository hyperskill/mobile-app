import SwiftUI

struct AuthLogoView: View {
    var logoWidthHeight: CGFloat = 48

    var title = Strings.Auth.Social.logInTitle

    var body: some View {
        VStack(spacing: 0) {
            HyperskillLogoView(logoWidthHeight: logoWidthHeight)

            Text(title)
                .font(.body)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)
                .padding(.top)
        }
    }
}

struct AuthLogoView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthLogoView()
                .preferredColorScheme(.light)

            AuthLogoView()
                .preferredColorScheme(.dark)
        }
    }
}
