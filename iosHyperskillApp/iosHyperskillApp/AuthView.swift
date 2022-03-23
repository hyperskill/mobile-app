import SwiftUI

extension AuthView {
    struct Appearance {
        var logoBorderColor = Color.black
        var logoBorderWidth = 1
        var logoSize = CGSize(width: 40, height: 40)

        var googleButtonForegroundColor = Color.purple
        var googleButtonMinHeight = 44
        var googleButtonOverlayCornerRadius = 8
        var googleButtonOverlayStrokeColor = Color.purple
        var googleButtonOverlayStrokeWidth = 2

    }
}

struct AuthView: View {
    let appearance: Appearance

    var body: some View {
        VStack {
            Image("logo")
                    .resizable()
                    .border(appearance.logoBorderColor, width: appearance.logoBorderWidth)
                    .frame(appearance.logoSize)
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
