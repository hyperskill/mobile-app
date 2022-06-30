import SwiftUI

struct RegisterView: View {
    var logoWidthHeight: CGFloat = 48

    var registerUrl: URL? = URL(string: "https://hyperskill.org/register")

    var body: some View {
        VStack(alignment: .leading, spacing: 48) {
            VStack(alignment: .center, spacing: 20) {
                Image(Images.AuthSocial.hyperskill)
                    .resizable()
                    .frame(width: logoWidthHeight, height: logoWidthHeight)

                Text(Strings.Register.title)
                    .font(.title2)
                    .foregroundColor(.primaryText)
            }
            .frame(maxWidth: .infinity)


            Text(Strings.Register.introText)
                .font(.body)
                .foregroundColor(.primaryText)

            if let registerUrl = registerUrl {
                Link(Strings.Register.buttonText, destination: registerUrl)
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }


            VStack(alignment: .leading, spacing: 20) {
                Text(Strings.Register.possibilityText)
                    .font(.body)
                    .foregroundColor(.primaryText)

                Text(Strings.Register.callText)
                    .font(.body)
                    .foregroundColor(.primaryText)
            }

            Spacer()
        }
        .padding()
    }
}

struct RegisterView_Previews: PreviewProvider {
    static var previews: some View {
        RegisterView()
    }
}
