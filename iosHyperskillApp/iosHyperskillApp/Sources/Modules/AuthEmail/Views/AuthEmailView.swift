import shared
import SwiftUI

extension AuthEmailView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let continueWithSocialButtonInsets = LayoutInsets(top: 24)

        let keyboardDistanceFromTextField: CGFloat = 60
    }
}

struct AuthEmailView: View {
    let appearance: Appearance

    @ObservedObject private var viewModel: AuthEmailViewModel

    @Environment(\.presentationMode) private var presentationMode

    init(viewModel: AuthEmailViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        AuthAdaptiveContentView { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthEmailFormView()

            Button(Strings.authEmailSocialText, action: { presentationMode.wrappedValue.dismiss() })
                .buttonStyle(OutlineButtonStyle(style: .violet))
                .padding(appearance.continueWithSocialButtonInsets.edgeInsets)
        }
        .onAppear {
            viewModel.startListening()
            KeyboardManager.setKeyboardDistanceFromTextField(appearance.keyboardDistanceFromTextField)
        }
        .onDisappear {
            viewModel.stopListening()
            KeyboardManager.setDefaultKeyboardDistanceFromTextField()
        }
        .navigationBarHidden(true)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthCredentialsFeatureActionViewAction) {}
}

struct AuthEmailView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
                .preferredColorScheme(.dark)

            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
        }
    }
}
