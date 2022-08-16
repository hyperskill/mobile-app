import shared
import SwiftUI

extension OnboardingView {
    struct Appearance {
        let logoWidth: CGFloat = 48
        let logoHeight: CGFloat = 44.85
    }
}

struct OnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: OnboardingViewModel

    let onSignInTap: () -> Void
    let onSignUpTap: () -> Void

    var body: some View {
        ZStack {
            BackgroundView()

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
                    .font(.largeTitle)
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
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
            viewModel.loadOnboarding()
        }
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: OnboardingFeatureActionViewAction) {
        print("OnboardingView :: \(#function) viewAction = \(viewAction)")
    }
}

struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingAssembly(onSignInTap: {}, onSignUpTap: {}).makeModule()

        OnboardingAssembly(onSignInTap: {}, onSignUpTap: {}).makeModule()
            .preferredColorScheme(.dark)
    }
}
