import SwiftUI

struct AuthSocialButton: View {
    let text: String
    let imageName: String
    private let action: () -> Void

    init(text: String, imageName: String, action: @escaping () -> Void) {
        self.text = text
        self.imageName = imageName
        self.action = action
    }

    var body: some View {
        Button(
            action: action,
            label: {
                Text(text)
                    .font(.subheadline)
                    .foregroundColor(.black.opacity(0.75))
                    .frame(maxWidth: .infinity, minHeight: 48, alignment: .center)
                    .background(Color.white)
                    .overlay(
                        Image(imageName)
                            .frame(width: 24, height: 24)
                            .padding(.leading)
                        ,
                        alignment: .leading
                    )
                    .cornerRadius(6)
            }
        )
        .shadow(color: Color.black.opacity(0.03), radius: 2, x: 0, y: 0)
        .shadow(color: Color.black.opacity(0.06), radius: 2, x: 0, y: 2)
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
