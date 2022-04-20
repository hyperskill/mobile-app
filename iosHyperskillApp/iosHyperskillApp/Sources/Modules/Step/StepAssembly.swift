import shared
import SwiftUI

final class StepAssembly: Assembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> StepView {
        let stepRepository = StepRepositoryImpl(
            stepRemoteDataSource: StepRemoteDataSourceImpl(
                httpClient: NetworkModule.shared.provideAuthorizedClient(
                    userAgentInfo: UserAgentBuilder.userAgentInfo,
                    json: NetworkModule.shared.provideJson(),
                    settings: Settings.shared.makeAppleSettings(userDefaults: UserDefaults.standard),
                    authorizationFlow: AuthDataBuilder.sharedAuthorizationFlow
                )
            )
        )
        let stepInteractor = StepInteractor(stepRepository: stepRepository)
        let stepFeature = StepFeatureBuilder.shared.build(stepInteractor: stepInteractor)

        let stepViewModel = StepViewModel(stepID: self.stepID, feature: stepFeature)

        return StepView(viewModel: stepViewModel)
    }
}
