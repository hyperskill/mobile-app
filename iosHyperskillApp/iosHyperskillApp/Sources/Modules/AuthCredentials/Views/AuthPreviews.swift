import SwiftUI

#if DEBUG
struct AuthPreviews_Previews: PreviewProvider {
    private static let previewDevices = [
        PreviewDevice(rawValue: "iPhone 13 Pro"),
        PreviewDevice(rawValue: "iPhone SE (3rd generation)"),
        PreviewDevice(rawValue: "iPad (9th generation)")
    ]

    static var previews: some View {
        ForEach(previewDevices, id: \.rawValue) { previewDevice in
            AuthCredentialsAssembly()
                .makeModule()
                .previewDevice(previewDevice)

            AuthSocialAssembly()
                .makeModule()
                .previewDevice(previewDevice)

            OnboardingAssembly()
                .makeModule()
                .previewDevice(previewDevice)

            AuthNewUserPlaceholderView(onSignInTap: {})
                .previewDevice(previewDevice)
        }
    }
}
#endif
