import shared
import SwiftUI
import UIKit

struct DebugView: View {
    @StateObject var viewModel: DebugViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            buildBody()
        }
        .navigationTitle(Strings.DebugMenu.title)
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            ProgressView()
                .onAppear {
                    viewModel.doLoadDebugSettings()
                }
        case .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(backgroundColor: .clear) {
                    viewModel.doLoadDebugSettings(forceUpdate: true)
                }
            )
        case .content(let data):
            buildContent(data: data)
        }
    }

    @ViewBuilder
    private func buildContent(data: DebugFeatureViewStateContent) -> some View {
        Form {
            Section(header: Text("")) {
                Picker(
                    Strings.DebugMenu.API.headerTitle,
                    selection: Binding<EndpointConfigType>(
                        get: { data.selectedEndpointConfig },
                        set: { viewModel.doSelectEndpointConfig($0) }
                    ),
                    content: {
                        ForEach(data.availableEndpointConfigs, id: \.self) { type in
                            if type != data.selectedEndpointConfig {
                                Text(type.name)
                                    .navigationTitle(Strings.DebugMenu.API.headerTitle)
                            } else {
                                Text(type.name)
                            }
                        }
                    }
                )

                if data.isApplySettingsButtonAvailable {
                    Section {
                        Button(Strings.DebugMenu.applySettingsButton, action: viewModel.doApplySettings)
                    }
                }
            }

            Section(header: Text(Strings.DebugMenu.StepNavigation.headerTitle)) {
                DebugStepNavigationView(
                    text: Binding<String>(
                        get: { data.navigationInput.step.stepId },
                        set: { viewModel.doStepNavigationInputChange(text: $0) }
                    ),
                    isOpenButtonEnabled: data.navigationInput.step.isOpenEnabled,
                    onOpenButtonTapped: viewModel.doStepNavigationOpenStep
                )
            }

            Section(header: Text(Strings.DebugMenu.StageImplement.headerTitle)) {
                DebugStageImplementNavigationView(
                    projectIdText: Binding<String>(
                        get: { data.navigationInput.stageImplement.projectId },
                        set: { viewModel.doStageImplementNavigationInputChange(projectIdText: $0) }
                    ),
                    stageIdText: Binding<String>(
                        get: { data.navigationInput.stageImplement.stageId },
                        set: { viewModel.doStageImplementNavigationInputChange(stageIdText: $0) }
                    ),
                    isOpenButtonEnabled: data.navigationInput.stageImplement.isOpenEnabled,
                    onOpenButtonTapped: viewModel.doStageImplementNavigationOpen
                )
            }
        }
    }

    private func handleViewAction(_ viewAction: DebugFeatureActionViewAction) {
        switch DebugFeatureActionViewActionKs(viewAction) {
        case .restartApplication:
            displayRestartApplicationAlert()
        case .openStep(let data):
            stackRouter.pushViewController(StepAssembly(stepRoute: data.stepRoute).makeModule())
        case .openStageImplement(let data):
            let assembly = StageImplementAssembly(projectID: data.projectId, stageID: data.stageId)
            stackRouter.pushViewController(assembly.makeModule())
        }
    }

    private func displayRestartApplicationAlert() {
        let alert = UIAlertController(
            title: Strings.DebugMenu.RestartApplication.Alert.title,
            message: Strings.DebugMenu.RestartApplication.Alert.message,
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.ok,
                style: .default,
                handler: { [weak viewModel] _ in
                    ProgressHUD.show()
                    viewModel?.doRestartApplication()
                }
            )
        )
        stackRouter.rootViewController?.present(alert, animated: true)
    }
}

struct DebugView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            DebugAssembly().makeModule()
        }
    }
}
