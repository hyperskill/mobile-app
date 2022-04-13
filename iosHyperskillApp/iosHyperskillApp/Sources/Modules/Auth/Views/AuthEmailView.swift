import SwiftUI

extension AuthEmailView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let contentMaxWidth: CGFloat = 400
    }
}

struct AuthEmailView: View {
    let appearance: Appearance

    @Binding var presentingContinueWithEmail: Bool

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    init(presentingContinueWithEmail: Binding<Bool>, appearance: Appearance = Appearance()) {
        self._presentingContinueWithEmail = presentingContinueWithEmail
        self.appearance = appearance
    }

    var body: some View {
        ZStack {
            Color(ColorPalette.background).ignoresSafeArea()

            VerticalCenteredScrollView(showsIndicators: false) {
                VStack(spacing: 0) {
                    if horizontalSizeClass == .regular {
                        Spacer()
                    }

                    AuthLogoView(logoWidthHeight: appearance.logoSize)
                        .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

                    AuthEmailFormView()

                    AuthOutlineButton(text: Strings.authSocialText, action: { presentingContinueWithEmail.toggle() })
                        .padding(.top)

                    Spacer()
                }
                .frame(maxWidth: appearance.contentMaxWidth)
            }.padding(.horizontal)
        }
        .navigationBarHidden(true)
    }
}

struct AuthEmailView_Previews: PreviewProvider {
    @State static var presentingContinueWithEmail = true
    static var previews: some View {
        AuthEmailView(presentingContinueWithEmail: $presentingContinueWithEmail)
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

//        AuthEmailView(presentingContinueWithEmail: $presentingContinueWithEmail)
//            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))

//        AuthEmailView(presentingContinueWithEmail: $presentingContinueWithEmail)
//            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
