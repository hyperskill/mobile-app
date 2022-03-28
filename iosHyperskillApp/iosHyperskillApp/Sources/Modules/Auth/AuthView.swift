import GoogleSignIn
import SwiftUI

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
    let appearance = Appearance()
    let viewModel = AuthViewModel()

    var body: some View {
        VStack {
            Image("logo")
                    .resizable()
                    .border(appearance.logoBorderColor, width: appearance.logoBorderWidth)
                    .frame(width: appearance.logoSize, height: appearance.logoSize)
                    .padding(.top)

            Spacer()

            VStack {
                Text("Log in to Hyperskill")
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
                                            RoundedRectangle(cornerRadius: appearance.googleButtonOverlayCornerRadius)
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
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthView()
    }
}
