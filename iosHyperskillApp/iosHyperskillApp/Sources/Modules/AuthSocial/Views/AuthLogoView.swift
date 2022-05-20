import SwiftUI

struct AuthLogoView: View {
    var logoWidthHeight: CGFloat = 48

    var body: some View {
        VStack(spacing: 0) {
            Image(Images.AuthSocial.hyperskill)
                .resizable()
                .frame(width: logoWidthHeight, height: logoWidthHeight)

            Text(Strings.authSocialLogInTitle)
                .font(.body)
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
