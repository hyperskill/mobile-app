import SwiftUI

extension OnboardingView {
    struct Appearance {
        let logoWidth: CGFloat = 48
        let logoHeight: CGFloat = 44.85
    }
}

struct OnboardingView: View {
    private(set) var appearance = Appearance()

    let onSignInTap: () -> Void
    let onSignUpTap: () -> Void

    var body: some View {
        ZStack {
            Color(ColorPalette.background)
                .ignoresSafeArea()

            VStack(alignment: .center, spacing: LayoutInsets.largeInset) {
                Image(Images.Onboarding.logo)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(
                        width: appearance.logoWidth,
                        height: appearance.logoHeight
                    )

                Text(Strings.Onboarding.title)
                    .font(.title)
                    .foregroundColor(.primaryText)

                Text(Strings.Onboarding.text)
                    .font(.body)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)


                Spacer()

                Image(Images.Onboarding.problemOfDayCard)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: .infinity)

                Spacer()

                Button(Strings.Onboarding.signIn) { onSignInTap() }
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(Strings.Onboarding.signUp) { onSignUpTap() }
                    .buttonStyle(OutlineButtonStyle(style: .violet))
            }
            .padding()
        }
    }
}

struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingView(onSignInTap: {}, onSignUpTap: {})

        OnboardingView(onSignInTap: {}, onSignUpTap: {})
            .preferredColorScheme(.dark)
    }
}
