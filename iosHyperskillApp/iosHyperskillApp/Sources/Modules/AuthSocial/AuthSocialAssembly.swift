import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    func makeModule() -> AuthSocialView {
        let authFeature = AuthFeatureBuilder.shared.build(authInteractor: .default)
        let authSocialViewModel = AuthSocialViewModel(feature: authFeature)
        return AuthSocialView(viewModel: authSocialViewModel)
    }
}

extension AuthInteractor {
    static var `default`: AuthInteractor {
        let authRepository = AuthRepositoryImpl(
            authCacheDataSource: AuthCacheDataSourceImpl(
                settings: Settings.shared.makeAppleSettings(userDefaults: UserDefaults.standard)
            ),
            authRemoteDataSource: AuthRemoteDataSourceImpl(
                deauthorizationFlow: AuthDataBuilder.sharedAuthorizationFlow,
                authSocialHttpClient: NetworkModule.shared.provideClient(
                    networkClientType: .social,
                    userAgentInfo: UserAgentBuilder.userAgentInfo,
                    json: NetworkModule.shared.provideJson()
                ),
                authCredentialsHttpClient: NetworkModule.shared.provideClient(
                    networkClientType: .credentials,
                    userAgentInfo: UserAgentBuilder.userAgentInfo,
                    json: NetworkModule.shared.provideJson()
                ),
                json: NetworkModule.shared.provideJson(),
                settings: Settings.shared.makeAppleSettings(userDefaults: UserDefaults.standard)
            )
        )
        return AuthInteractor(authRepository: authRepository)
    }
}

extension AuthDataBuilder {
    static let sharedAuthorizationFlow = AuthDataBuilder.shared.provideAuthorizationFlow()
}
