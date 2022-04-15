import SwiftUI

extension AuthEmailView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let contentViewInsets = LayoutInsets(horizontal: LayoutInsets.defaultInset).edgeInsets

        let continueWithSocialButtonInsets = LayoutInsets(top: 24)

        let keyboardDistanceFromTextField: CGFloat = 60
    }
}

struct AuthEmailView: View {
    private(set) var appearance = Appearance()

    @Binding var presentingContinueWithEmail: Bool

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        AuthAdaptiveContentView(
            appearance: .init(insets: appearance.contentViewInsets)
        ) { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthEmailFormView()

            Button(Strings.authEmailSocialText, action: { presentingContinueWithEmail = false })
                .buttonStyle(OutlineButtonStyle(style: .violet))
                .padding(appearance.continueWithSocialButtonInsets.edgeInsets)
        }
        .onAppear {
            IQKeyboardManagerConfigurator.setKeyboardDistanceFromTextField(appearance.keyboardDistanceFromTextField)
        }
        .onDisappear {
            IQKeyboardManagerConfigurator.setDefaultKeyboardDistanceFromTextField()
        }
    }
}

struct AuthEmailView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthEmailView(presentingContinueWithEmail: .constant(true))
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

            if #available(iOS 15.0, *) {
                AuthEmailView(presentingContinueWithEmail: .constant(true))
                    .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
                    .previewInterfaceOrientation(.landscapeRight)
            }
            AuthEmailView(presentingContinueWithEmail: .constant(true))
                .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))

            AuthEmailView(presentingContinueWithEmail: .constant(true))
                .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
        }

        Group {
            AuthEmailView(presentingContinueWithEmail: .constant(true))
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
        }
        .preferredColorScheme(.dark)
    }
}
