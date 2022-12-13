import SwiftUI

extension AuthNewUserPlaceholderHeaderView {
    struct Appearance {
        var spacing: CGFloat = 32

        let logoWidthHeight: CGFloat = 48
    }
}

struct AuthNewUserPlaceholderHeaderView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: appearance.spacing) {
            VStack(spacing: LayoutInsets.largeInset) {
                HyperskillLogoView(logoWidthHeight: appearance.logoWidthHeight)

                Text(Strings.Auth.NewUserPlaceholder.title)
                    .font(.title2)
                    .bold()
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)
            }
            .frame(maxWidth: .infinity)

            Text(Strings.Auth.NewUserPlaceholder.subtitle)
                .font(.body)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)
        }
    }
}

struct AuthNewUserPlaceholderHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderHeaderView()
    }
}
