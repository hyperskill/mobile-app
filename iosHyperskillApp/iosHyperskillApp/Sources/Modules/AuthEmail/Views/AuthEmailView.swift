import SwiftUI

extension AuthEmailView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let contentMaxWidth: CGFloat = 400

        let keyboardDistanceFromTextField: CGFloat = 60
    }
}

struct AuthEmailView: View {
    private(set) var appearance = Appearance()

    @Binding var presentingContinueWithEmail: Bool

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        ZStack {
            BackgroundView()

            VerticalCenteredScrollView(showsIndicators: false) {
                VStack(spacing: 0) {
                    if horizontalSizeClass == .regular {
                        Spacer()
                    }

                    AuthLogoView(logoWidthHeight: appearance.logoSize)
                        .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

                    AuthEmailFormView()

                    Button(Strings.authSocialText, action: { presentingContinueWithEmail.toggle() })
                        .buttonStyle(OutlineButtonStyle(style: .violet))
                        .padding(.top)

                    Spacer()
                }
                .frame(maxWidth: appearance.contentMaxWidth)
            }
            .padding(.horizontal)
        }
        .navigationBarHidden(true)
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
