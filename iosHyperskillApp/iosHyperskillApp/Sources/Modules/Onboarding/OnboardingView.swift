import shared
import SwiftUI

extension OnboardingView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48

        let contentMaxWidth: CGFloat = 400
    }
}

struct OnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: OnboardingViewModel

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        ZStack {
            BackgroundView()

            VStack(alignment: .center, spacing: LayoutInsets.largeInset) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                HyperskillLogoView(logoWidthHeight: appearance.logoWidthHeight)

                Text(Strings.Onboarding.title)
                    .font(.largeTitle)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)

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

                Button(Strings.Onboarding.signIn, action: viewModel.doSignPresentation)
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(Strings.Onboarding.signUp, action: viewModel.doSignUpPresentation)
                    .buttonStyle(OutlineButtonStyle(style: .violet))

                if horizontalSizeClass == .regular {
                    Spacer()
                }
            }
            .frame(maxWidth: appearance.contentMaxWidth)
            .padding()
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)

            viewModel.loadOnboarding()
            viewModel.logViewedEvent()
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
        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        OnboardingAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
