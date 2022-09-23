import SwiftUI

extension AuthSocialButton {
    struct Appearance {
        let textFont = Font.subheadline
        var textColor = Color.black.opacity(0.87)

        let imageIconSize = CGSize(width: 24, height: 24)

        let height: CGFloat = 48

        let borderColor = UIColor.dynamic(light: ColorPalette.onSurfaceAlpha38, dark: .clear)
        let cornerRadius: CGFloat = 6
    }
}

struct AuthSocialButton: View {
    let text: String
    let imageName: String
    let action: () -> Void

    private(set) var appearance = Appearance()

    var body: some View {
        Button(
            action: action,
            label: {
                Text(text)
                    .font(appearance.textFont)
                    .foregroundColor(appearance.textColor)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .frame(height: appearance.height)
                    .background(Color.white)
                    .overlay(
                        Image(imageName)
                            .frame(size: appearance.imageIconSize)
                            .padding(.leading)
                        ,
                        alignment: .init(horizontal: .leading, vertical: .center)
                    )
            }
        )
        .addBorder(color: Color(appearance.borderColor), cornerRadius: appearance.cornerRadius)
        .buttonStyle(BounceButtonStyle())
    }
}

struct AuthSocialButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthSocialButton(text: "Google", imageName: Images.AuthSocial.google, action: {})
                .padding(.horizontal)
                .preferredColorScheme(.light)

            AuthSocialButton(text: "Google", imageName: Images.AuthSocial.google, action: {})
                .padding(.horizontal)
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
