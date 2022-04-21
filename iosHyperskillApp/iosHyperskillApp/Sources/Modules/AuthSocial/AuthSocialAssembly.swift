import shared
import SwiftUI

final class AuthSocialAssembly: Assembly {
    func makeModule() -> AuthSocialView {
        let feature = AuthSocialFeatureBuilder.shared.build(authInteractor: .default)
        let viewModel = AuthSocialViewModel(socialAuthService: SocialAuthService.shared, feature: feature)
        return AuthSocialView(viewModel: viewModel)
    }
}

extension AuthInteractor {
    static var `default`: AuthInteractor {
        let authRepository = AuthRepositoryImpl(
            authCacheDataSource: AuthCacheDataSourceImpl(settings: Settings.default),
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
                settings: Settings.default
            )
        )
        return AuthInteractor(authRepository: authRepository)
    }
}
