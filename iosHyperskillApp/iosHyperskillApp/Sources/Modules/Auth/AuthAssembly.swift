import shared
import SwiftUI

final class AuthAssembly: Assembly {
    func makeModule() -> AuthView {
        let authRepository = AuthRepositoryImpl(
            authCacheDataSource: AuthCacheDataSourceImpl(
                settings: Settings.shared.makeAppleSettings(userDefaults: UserDefaults.standard)
            ),
            authRemoteDataSource: AuthRemoteDataSourceImpl(
                authHttpClient: NetworkModule.shared.provideAuthClient(
                    userAgentInfo: UserAgentBuilder.userAgentInfo,
                    json: NetworkModule.shared.provideJson()
                ),
                json: NetworkModule.shared.provideJson(),
                settings: Settings.shared.makeAppleSettings(userDefaults: UserDefaults.standard)
            )
        )
        let authInteractor = AuthInteractor(authRepository: authRepository)
        let authFeature = AuthFeatureBuilder.shared.build(authInteractor: authInteractor)

        let authViewModel = AuthViewModel(feature: authFeature)

        return AuthView(viewModel: authViewModel)
    }
}
