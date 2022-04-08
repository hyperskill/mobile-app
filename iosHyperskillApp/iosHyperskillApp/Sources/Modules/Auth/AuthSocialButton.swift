import SwiftUI

struct AuthSocialButton: View {
    let text: String
    let imageName: String
    let action: () -> Void

    init(text: String, imageName: String, action: @escaping () -> Void) {
        self.text = text
        self.imageName = imageName
        self.action = action
    }


    var body: some View {
        Button(
            action: {
                action()
            },
            label: {
                Text(text)
                    .font(.subheadline)
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity, minHeight: 48, alignment: .center)
                    .background(Color.white)
                    .overlay(
                        Image(imageName)
                            .frame(width: 24, height: 24)
                            .padding(.leading)
                        ,
                        alignment: .leading
                    )
            }
        )
        .cornerRadius(6)
        .padding(.horizontal, 20)
        .shadow(color: Color.black.opacity(0.03), radius: 2, x: 0, y: 0)
        .shadow(color: Color.black.opacity(0.06), radius: 2, x: 0, y: 2)
    }
}


