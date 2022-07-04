import SwiftUI

extension NewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48
        let bigPadding: CGFloat = 48
        let smallPadding: CGFloat = 20
    }
}

struct NewUserPlaceholderView: View {
    private(set) var appearance = Appearance()

    var registerUrl: URL? = URL(string: "https://hyperskill.org/register")

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.bigPadding) {
            VStack(alignment: .center, spacing: appearance.smallPadding) {
                Image(Images.AuthSocial.hyperskill)
                    .resizable()
                    .frame(widthHeight: appearance.logoWidthHeight)

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


            VStack(alignment: .leading, spacing: appearance.smallPadding) {
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
        NewUserPlaceholderView()
    }
}
