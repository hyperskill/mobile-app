import SwiftUI

struct AuthSocialButton: View {
    let viewModel: AuthViewModel
    let provider: SocialProvider


    var body: some View {
        Button(
            action: {
                viewModel.signInWithSocialProvider(provider: provider)
            },
            label: {
                HStack {
                    Image(getIcon()).padding()
                    Spacer()
                    Text(getText())
                        .font(.subheadline)
                        .foregroundColor(.black)
                    Spacer()
                }.background(Color.white)
            }
        ).padding(.horizontal, 20)
    }

    func getIcon() -> String {
    switch provider {
    case .google:
        return "google_icon"
    case .jetbrains:
        return "jb_icon"
    case .apple:
        return "apple_icon"
    case .github:
        return "github_icon"
    }
    }

    func getText() -> String {
        switch provider {
        case .google:
            return "Google"
        case .jetbrains:
            return "JetBrains Account"
        case .apple:
            return "Apple"
        case .github:
            return "GitHub"
        }
    }
}
