import SwiftUI

struct StepQuizFillBlanksView: View {
    @StateObject var viewModel: StepQuizFillBlanksViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        FillBlanksQuizViewWrapper(
            components: viewModel.viewData.components,
            isUserInteractionEnabled: isEnabled,
            onInputDidChange: viewModel.doInputTextUpdate(_:for:),
            onDidSelectComponent: viewModel.doSelectComponent(at:),
            onDidDeselectComponent: viewModel.doDeselectComponent(at:)
        )
        .onAppear {
            viewModel.doProvideModuleInput()
            KeyboardManager.setEnableAutoToolbar(true)
        }
        .onDisappear {
            KeyboardManager.setEnableAutoToolbar(false)
        }
        .opacity(isEnabled ? 1 : 0.5)
    }
}
