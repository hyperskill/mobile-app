import GoogleSignIn
import SwiftUI


extension AuthView {
    struct Appearance {
        var logoBorderColor = Color.black
        var logoBorderWidth: CGFloat = 1
        var logoSize: CGFloat = 40

        var googleButtonForegroundColor = Color.purple
        var googleButtonMinHeight: CGFloat = 44
        var googleButtonOverlayCornerRadius: CGFloat = 8
        var googleButtonOverlayStrokeColor = Color.purple
        var googleButtonOverlayStrokeWidth: CGFloat = 2

    }
}

struct AuthView: View {
    let appearance: Appearance = Appearance()

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
                        action: {},
                        label: {
                            Text("Google")
                                    .font(.body)
                                    .foregroundColor(appearance.googleButtonForegroundColor)
                                    .frame(minHeight: appearance.googleButtonMinHeight)
                                    .padding(.horizontal)
                                    .overlay(
                                            RoundedRectangle(cornerRadius: appearance.googleButtonOverlayCornerRadius)
                                                    .stroke(appearance.googleButtonOverlayStrokeColor, lineWidth: appearance.googleButtonOverlayStrokeWidth)
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
