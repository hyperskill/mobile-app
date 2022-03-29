import SwiftUI
import shared

extension AuthView {
    struct Appearance {
        let logoBorderColor = Color.black
        let logoBorderWidth: CGFloat = 1
        let logoSize: CGFloat = 40

        let googleButtonForegroundColor = Color.purple
        let googleButtonMinHeight: CGFloat = 44
        let googleButtonOverlayCornerRadius: CGFloat = 8
        let googleButtonOverlayStrokeColor = Color.purple
        let googleButtonOverlayStrokeWidth: CGFloat = 2
    }
}

struct AuthView: View {
    @ObservedObject private var viewModel: AuthViewModel

    let appearance: Appearance

    init(viewModel: AuthViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }
    
    var body: some View {
        VStack {
            Image("logo")
                .resizable()
                .border(appearance.logoBorderColor, width: appearance.logoBorderWidth)
                .frame(width: appearance.logoSize, height: appearance.logoSize)
                .padding(.top)

            Spacer()

            VStack {
                Text(Strings.authLogInTitle)
                    .font(.title)
                    .bold()

                Button(
                    action: viewModel.signInWithGoogle,
                    label: {
                        Text("Google")
                            .font(.body)
                            .foregroundColor(appearance.googleButtonForegroundColor)
                            .frame(minHeight: appearance.googleButtonMinHeight)
                            .padding(.horizontal)
                            .overlay(
                                RoundedRectangle(
                                    cornerRadius: appearance.googleButtonOverlayCornerRadius
                                )
                                .stroke(
                                    appearance.googleButtonOverlayStrokeColor,
                                    lineWidth: appearance.googleButtonOverlayStrokeWidth
                                )
                            )
                    }
                )
                .padding(.top)
            }

            Spacer()
            Spacer()
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthFeatureActionViewAction) {
        print("AuthView :: \(#function) viewAction = \(viewAction)")
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthAssembly().makeModule()
    }
}
