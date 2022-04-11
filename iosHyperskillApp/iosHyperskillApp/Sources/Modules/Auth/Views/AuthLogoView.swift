import SwiftUI

struct AuthLogoView: View {
    let logoWidthHeight: CGFloat

    var body: some View {
        VStack(spacing: 0) {
            Image("logo")
                .resizable()
                .frame(width: logoWidthHeight, height: logoWidthHeight)

            Text(Strings.authLogInTitle)
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.top)
        }
    }
}

struct AuthLogoView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthLogoView(logoWidthHeight: 48)
                .preferredColorScheme(.light)

            AuthLogoView(logoWidthHeight: 48)
                .preferredColorScheme(.dark)
        }
    }
}
