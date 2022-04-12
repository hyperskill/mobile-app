import shared
import SwiftUI

final class StepAssembly: Assembly {
    func makeModule() -> StepView {
        let stepRepository = StepRepositoryImpl(
            stepRemoteDataSource: StepRemoteDataSourceImpl(
                httpClient: NetworkModule.shared.provideClient(json: NetworkModule.shared.provideJson())
            )
        )
        let stepInteractor = StepInteractor(stepRepository: stepRepository)
        let stepFeature = StepFeatureBuilder.shared.build(stepInteractor: stepInteractor)

        let stepViewModel = StepViewModel(feature: stepFeature)

        return StepView(viewModel: stepViewModel)
    }
}
