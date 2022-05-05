import shared
import SwiftUI

final class StepAssembly: Assembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> StepView {
        let repository = StepRepositoryImpl(
            stepRemoteDataSource: StepRemoteDataSourceImpl(
                httpClient: NetworkModule.shared.provideAuthorizedClient(
                    userAgentInfo: UserAgentBuilder.userAgentInfo,
                    json: NetworkModule.shared.provideJson(),
                    settings: Settings.default,
                    authorizationFlow: AuthDataBuilder.sharedAuthorizationFlow,
                    authorizationMutex: AuthDataBuilder.sharedAuthorizationMutex
                )
            )
        )
        let interactor = StepInteractor(stepRepository: repository)
        let feature = StepFeatureBuilder.shared.build(stepInteractor: interactor)

        let viewModel = StepViewModel(stepID: self.stepID, feature: feature)

        return StepView(viewModel: viewModel)
    }
}
